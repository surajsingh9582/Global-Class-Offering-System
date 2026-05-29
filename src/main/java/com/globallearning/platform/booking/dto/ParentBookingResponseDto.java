package com.globallearning.platform.booking.dto;

import java.time.Instant;

public record ParentBookingResponseDto(
        Long bookingId,
        Long offeringId,
        String courseTitle,
        Instant createdAt
) {
}
