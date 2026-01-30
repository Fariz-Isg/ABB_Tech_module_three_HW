package org.abbtech.module3.dto;

import jakarta.validation.constraints.*;

public record CarRequestDto(

        @NotNull(message = "Model ID is required")
        @Positive(message = "Model ID must be positive")
        Long modelId,

        @NotBlank(message = "Color is required")
        @Size(min = 2, max = 50, message = "Color must be between 2 and 50 characters")
        String color,

        @NotNull(message = "Year is required")
        @Min(value = 1900, message = "Year must be after 1900")
        @Max(value = 2100, message = "Year must be before 2100")
        Integer year,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price,

        @NotNull(message = "Speed is required")
        @Min(value = 0, message = "Speed must be non-negative")
        @Max(value = 500, message = "Speed must not exceed 500 km/h")
        Integer speed,

        @Size(max = 50, message = "VIN number must not exceed 50 characters")
        String vinNumber
) {
}