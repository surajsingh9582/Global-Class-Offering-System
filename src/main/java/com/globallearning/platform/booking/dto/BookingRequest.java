package com.globallearning.platform.booking.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull Long parentId,
        @NotNull Long offeringId
) {}
