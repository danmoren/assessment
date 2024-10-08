package com.sky.assessment.controller.response;

import java.util.List;
import lombok.Builder;

/**
 * ErrorResponse class.
 */
@Builder
public record ErrorResponse(String message, List<String> details) {}
