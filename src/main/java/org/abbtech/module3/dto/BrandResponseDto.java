package org.abbtech.module3.dto;

import java.time.LocalDateTime;

public record BrandResponseDto(
        Long id,
        String name,
        String country,
        Integer foundedYear,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}