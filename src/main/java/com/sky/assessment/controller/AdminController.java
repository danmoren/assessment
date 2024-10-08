package com.sky.assessment.controller;

import com.sky.assessment.controller.request.UserProjectRequest;
import com.sky.assessment.controller.request.UserRequest;
import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(value = "/admin")
public class AdminController {

  private final IUserService userService;

  @Operation(summary = "Creates a new user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Information of the user created successfully"),
      @ApiResponse(responseCode = "409", description = "User already exists"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @PostMapping(value = "/users/create")
  public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
    return ResponseEntity.ok(userService.createUser(userRequest));
  }

  @Operation(summary = "Add a project to an existent user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Information of the project created successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @PostMapping(value = "/users/{userId}/project")
  public UserProjectResponse addProject(@PathVariable Long userId,
      @Valid @RequestBody UserProjectRequest userProjectRequest) {
    return userService.addProject(userId, userProjectRequest);
  }

  @Operation(summary = "Get user data of a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User data retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @GetMapping(value = "/users/{id}")
  public UserResponse getUserData(@PathVariable Long id) {
    return userService.getUserData(id);
  }

  @Operation(summary = "Delete a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @DeleteMapping(value = "/users/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }

  @Operation(summary = "Get the projects of a user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User projects retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  @GetMapping(value = "/users/{id}/project")
  public List<UserProjectResponse> getProjects(@PathVariable Long id) {
    return userService.getProjectsByUser(id);
  }

}
