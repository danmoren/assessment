package com.sky.assessment.controller;

import com.sky.assessment.controller.response.UserProjectResponse;
import com.sky.assessment.controller.response.UserResponse;
import com.sky.assessment.service.IUserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(value = "/users")
@OpenAPIDefinition(
    info = @Info(
        description = "REST API for SKY assessment project",
        title = "SKY Assessment API",
        version = "0.0.1"
    )
)
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Get user data of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User data retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/me")
    public UserResponse getUserData(Authentication authentication) {
        return userService.getUserData(userService
            .getUserByEmail(authentication.getName()).id());
    }

    @Operation(summary = "Delete the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @DeleteMapping(value = "/me")
    public void deleteUser(Authentication authentication) {
        userService.deleteUser(userService.getUserByEmail(authentication.getName()).id());
    }

    @Operation(summary = "Get the projects of the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User projects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "/me/projects")
    public List<UserProjectResponse> getProjects(Authentication authentication) {
        return userService.getProjectsByUser(userService
            .getUserByEmail(authentication.getName()).id());
    }

}
