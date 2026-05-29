package com.globallearning.platform.booking.dto;

public record AvailableOfferingResponseDto(
        Long id,
        Integer capacity,
        String courseTitle
) {
}
