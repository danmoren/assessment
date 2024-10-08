package com.sky.assessment.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

/**
 * Request object for creating a new user.
 */
@Builder
public record UserRequest(
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email address should be valid")
    @Size(max = 200, message = "Email cannot be more than 200 characters long") String email,
    @Size(max = 120, message = "Name cannot be more than 120 characters long") String name,
    @NotNull
    @Size(max = 129, message = "Password cannot be more than 129 characters long") String password,
    @NotNull(message = "Roles cannot be null") List<String> roles) {}
