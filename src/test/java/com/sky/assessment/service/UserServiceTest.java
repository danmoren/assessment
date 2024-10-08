package com.sky.assessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.entity.Role;
import com.sky.assessment.entity.User;
import com.sky.assessment.exception.ResourceNotFoundException;
import com.sky.assessment.exception.UserAlreadyExistsException;
import com.sky.assessment.repository.RoleRepository;
import com.sky.assessment.repository.UserRepository;
import com.sky.assessment.service.impl.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetUserData_UserExists() {
    User user = new User();
    user.setId(1L);
    user.setRoles(new HashSet<>());
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    UserResponse response = userService.getUserData(1L);

    assertNotNull(response);
    assertEquals(1L, response.id());
  }

  @Test
  void testGetUserData_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userService.getUserData(1L));
  }

  @Test
  void testCreateUser_UserAlreadyExists() {
    UserRequest userRequest = new UserRequest("test@example.com", "Test User",
        "password", List.of("USER", "ADMIN"));
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userRequest));
  }

  @Test
  void testCreateUser_Success() {
    UserRequest userRequest = new UserRequest("test@example.com", "Test User",
        "password", List.of("USER"));
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

    Role role = new Role();
    role.setId(1L);
    role.setName("USER");
    when(roleRepository.findByName(any())).thenReturn(Optional.of(role));

    User user = new User();
    user.setEmail("test@example.com");
    user.setRoles(new HashSet<>());
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserResponse response = userService.createUser(userRequest);

    assertNotNull(response);
    assertEquals("test@example.com", response.email());
  }
}
