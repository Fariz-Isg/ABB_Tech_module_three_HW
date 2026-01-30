package org.abbtech.module3.dto;

import jakarta.validation.constraints.*;

public record BrandRequestDto(
        @NotBlank(message = "Brand name is required")
        @Size(min = 2, max = 100, message = "Brand name must be between 2 and 100 characters")
        String name,

        @Size(max = 50, message = "Country name must not exceed 50 characters")
        String country,

        @Min(value = 1800, message = "Founded year must be after 1800")
        @Max(value = 2100, message = "Founded year must be before 2100")
        Integer foundedYear
) {
}