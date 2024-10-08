package com.sky.assessment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.assessment.configuration.SecurityConfig;
import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.exception.UserAlreadyExistsException;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {

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
  void testCreateUser_Success() throws Exception {
    UserRequest userRequest = new UserRequest("user@test.com", "Test User",
        "password", List.of("USER"));
    UserResponse userResponse = new UserResponse(1L, "Test User", "user@test.com",
        List.of("USER"));

    when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

    mockMvc.perform(post("/admin/users/create")
            .with(httpBasic("admin@test.com", "test_password"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("user@test.com"));
  }

  @Test
  void testCreateUser_WrongRole() throws Exception {
    UserRequest userRequest = new UserRequest("user@test.com", "Test User", "password",
        List.of("USER"));
    mockMvc.perform(post("/admin/users/create")
            .with(httpBasic("user@test.com", "test_password"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  void testCreateUser_UserAlreadyExists() throws Exception {
    UserRequest userRequest = new UserRequest("test@example.com", "Test User", "password",
        List.of("USER"));

    doThrow(new UserAlreadyExistsException("User already exists with email: test@example.com"))
        .when(userService).createUser(any(UserRequest.class));

    mockMvc.perform(post("/admin/users/create")
            .with(httpBasic("admin@test.com", "test_password"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("User already exists with email: test@example.com"));
  }

}
