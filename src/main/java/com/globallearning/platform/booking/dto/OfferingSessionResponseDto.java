package com.globallearning.platform.booking.dto;

import java.util.List;

public record OfferingSessionResponseDto(
        Long id,
        Integer capacity,
        List<SessionResponseDto> sessions
) {
}
