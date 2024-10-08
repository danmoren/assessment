package com.sky.assessment.controller.response;

import java.util.List;
import lombok.Builder;

/**
 * UserResponse class.
 */
@Builder
public record UserResponse(Long id, String name, String email, List<String> roles) {}
