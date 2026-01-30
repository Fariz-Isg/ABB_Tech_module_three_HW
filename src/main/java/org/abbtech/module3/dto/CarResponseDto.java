package org.abbtech.module3.dto;

import java.time.LocalDateTime;

public record CarResponseDto(
        Long id,
        String color,
        Integer year,
        Double price,
        Integer speed,
        String vinNumber,
        Long modelId,
        String modelName,
        String brandName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}