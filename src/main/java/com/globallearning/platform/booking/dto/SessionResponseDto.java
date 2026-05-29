package com.globallearning.platform.booking.dto;

import java.time.Instant;

public record SessionResponseDto(
        Long id,
        Instant startTime,
        Instant endTime
) {
}
