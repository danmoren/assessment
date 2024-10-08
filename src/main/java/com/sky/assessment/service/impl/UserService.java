package com.sky.assessment.service.impl;

import com.sky.assessment.controller.request.UserProjectRequest;
import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.entity.Role;
import com.sky.assessment.entity.User;
import com.sky.assessment.entity.UserProject;
import com.sky.assessment.exception.ResourceNotFoundException;
import com.sky.assessment.exception.UserAlreadyExistsException;
import com.sky.assessment.mapper.Mapper;
import com.sky.assessment.repository.RoleRepository;
import com.sky.assessment.repository.UserProjectRepository;
import com.sky.assessment.repository.UserRepository;
import com.sky.assessment.service.IUserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final UserRepository userRepository;
  private final UserProjectRepository userProjectRepository;
  private final RoleRepository roleRepository;

  /**
   * Get user by email
   * @param email
   * @return {@link UserResponse}
   */
  @Override
  public UserResponse getUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return Mapper.INSTANCE.toUserResponse(user);
  }

  /**
   * Get user data by id
   * @param id
   * @return {@link UserResponse}
   */
  @Override
  public UserResponse getUserData(Long id) {
    log.info("Getting user data for id '{}'", id);
    return Mapper.INSTANCE.toUserResponse(userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id)));
  }


  /**
   * Create user
   * @param userRequest
   * @return {@link UserResponse}
   */
  @Override
  public UserResponse createUser(UserRequest userRequest) {
    log.info("Creating user with email '{}'", userRequest.email());

    if (userRepository.findByEmail(userRequest.email()).isPresent()) {
      throw new UserAlreadyExistsException("User already exists with email: " + userRequest.email());
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(userRequest.password());
    User newUser = Mapper.INSTANCE.toUserEntity(userRequest);
    newUser.setPassword(hashedPassword);

    Set<Role> roles = userRequest.roles().stream()
        .map(roleName -> roleRepository.findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName)))
        .collect(Collectors.toSet());
    newUser.setRoles(roles);

    return Mapper.INSTANCE.toUserResponse(
        userRepository.save(newUser));
  }

  /**
   * Delete user
   * @param id
   */
  @Override
  public void deleteUser(Long id) {
    log.info("Deleting user with id '{}'", id);
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id " + id);
    }
    userRepository.deleteById(id);
  }

  /**
   * Add project to user
   * @param id
   * @param userProjectRequest
   * @return {@link UserProjectResponse}
   */
  @Override
  public UserProjectResponse addProject(Long id, UserProjectRequest userProjectRequest) {
    log.info("Adding project for user with id '{}'", id);
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id " + id);
    }
    UserProject newUserProject = Mapper.INSTANCE.toUserProjectEntity(userProjectRequest);
    newUserProject.setUserId(id);
    return Mapper.INSTANCE.toUserProjectResponse(userProjectRepository.save(newUserProject));
  }

  /**
   * Get projects by user
   * @param id
   * @return {@link List<UserProjectResponse>}
   */
  @Override
  public List<UserProjectResponse> getProjectsByUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("User not found with id " + id);
    }
    return Mapper.INSTANCE.toUserProjectResponseList(userProjectRepository.findByUserId(id));
  }
}
