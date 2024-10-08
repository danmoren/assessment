package com.sky.assessment.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request object for creating a new project.
 */
public record UserProjectRequest(
    @NotNull(message = "Project name cannot be null")
    @Size(max = 120, message = "Project name cannot be more than 200 characters long") String name) {}
