package com.globallearning.platform.booking.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOfferingRequest(
        @NotNull Long courseId,
        @NotNull Long teacherId,
        @NotNull Integer capacity,
        @NotNull List<SessionDto>sessions
) {}
