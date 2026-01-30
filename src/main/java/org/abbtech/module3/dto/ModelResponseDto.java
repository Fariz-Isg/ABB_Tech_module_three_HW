package org.abbtech.module3.dto;

import java.time.LocalDateTime;

public record ModelResponseDto(
        Long id,
        String name,
        Integer modelYear,
        String bodyType,
        Long brandId,
        String brandName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}