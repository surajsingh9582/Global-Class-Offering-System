package com.globallearning.platform.booking.dto;

import java.time.Instant;

public record BookingResponseDto(
        Long bookingId,
        Long offeringId,
        String courseTitle,
        Instant createdAt
) {
}
