package com.sky.assessment.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sky.assessment.configuration.SecurityConfig;
import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.service.CustomUserDetailsService;
import com.sky.assessment.service.IUserService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IUserService userService;

  @MockBean
  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    UserDetails adminUser = new org.springframework.security.core.userdetails.User(
        "admin@test.com",
        "$2a$10$iDyHRW1PZnofpYSDKZVfMuvZh1W7K0mpWouirNjdTi9ln4JDoGkY6",
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
    );

    UserDetails user = new org.springframework.security.core.userdetails.User(
        "user@test.com",
        "$2a$10$iDyHRW1PZnofpYSDKZVfMuvZh1W7K0mpWouirNjdTi9ln4JDoGkY6",
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
    );

    UserDetails userWrongRole = new org.springframework.security.core.userdetails.User(
        "wrong@test.com",
        "$2a$10$iDyHRW1PZnofpYSDKZVfMuvZh1W7K0mpWouirNjdTi9ln4JDoGkY6",
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_UNKNOWN"))
    );
    when(customUserDetailsService.loadUserByUsername("admin@test.com")).thenReturn(adminUser);
    when(customUserDetailsService.loadUserByUsername("user@test.com")).thenReturn(user);
    when(customUserDetailsService.loadUserByUsername("wrong@test.com")).thenReturn(userWrongRole);
  }

  @Test
  void getAdminInfo_Success() throws Exception {
    UserResponse userResponse = new UserResponse(1L, "Test Admin", "admin@test.com",
        List.of("ADMIN"));

    when(userService.getUserByEmail("admin@test.com")).thenReturn(userResponse);
    when(userService.getUserData(1L)).thenReturn(userResponse);

    mockMvc.perform(get("/users/me")
            .with(httpBasic("admin@test.com", "test_password")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("admin@test.com"));
  }

  @Test
  void getUserInfo_Success() throws Exception {
    UserResponse userResponse = new UserResponse(1L, "Test User", "user@test.com", List.of("USER"));

    when(userService.getUserByEmail("user@test.com")).thenReturn(userResponse);
    when(userService.getUserData(1L)).thenReturn(userResponse);

    mockMvc.perform(get("/users/me")
            .with(httpBasic("user@test.com", "test_password")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("user@test.com"));
  }

  @Test
  void getUserInfo_WrongRole() throws Exception {
    UserResponse userResponse = new UserResponse(1L, "Test Wrong", "wrong@test.com",
        List.of("ROLE_UNKNOWN"));

    when(userService.getUserByEmail("wrong@test.com")).thenReturn(userResponse);
    when(userService.getUserData(1L)).thenReturn(userResponse);

    mockMvc.perform(get("/users/me")
            .with(httpBasic("wrong@test.com", "test_password")))
        .andExpect(status().isForbidden());
  }


  @Test
  void deleteUser_Success() throws Exception {
    doNothing().when(userService).deleteUser(1L);

    when(userService.getUserByEmail("user@test.com")).thenReturn(
        new UserResponse(1L, "Test User", "user@test.com", List.of("USER")));

    mockMvc.perform(delete("/users/me")
            .with(httpBasic("user@test.com", "test_password")))
        .andExpect(status().isOk());
  }

  @Test
  void getProjects_Success() throws Exception {
    List<UserProjectResponse> projects = List.of(new UserProjectResponse(1L, "Project 1"),
        new UserProjectResponse(2L, "Project 2"));

    when(userService.getUserByEmail("user@test.com")).thenReturn(
        new UserResponse(1L, "Test User", "user@test.com", List.of("USER")));
    when(userService.getProjectsByUser(1L)).thenReturn(projects);

    mockMvc.perform(get("/users/me/projects")
            .with(httpBasic("user@test.com", "test_password")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Project 1"))
        .andExpect(jsonPath("$[1].name").value("Project 2"));
  }

}
