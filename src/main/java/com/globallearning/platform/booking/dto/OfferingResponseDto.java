package com.globallearning.platform.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record OfferingResponseDto(
        Long id,
        Long courseId,
        Long teacherId,
        Integer capacity
){
}
