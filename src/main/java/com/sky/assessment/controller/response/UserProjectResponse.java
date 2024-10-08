package com.sky.assessment.controller.response;

import lombok.Builder;

/**
 * UserProjectResponse class.
 */
@Builder
public record UserProjectResponse(Long id, String name) {}
