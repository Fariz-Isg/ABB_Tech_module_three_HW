package org.abbtech.module3.dto;

import jakarta.validation.constraints.*;

public record ModelRequestDto(

        @NotBlank(message = "Model name is required")
        @Size(min = 1, max = 100, message = "Model name must be between 1 and 100 characters")
        String name,

        @NotNull(message = "Brand ID is required")
        @Positive(message = "Brand ID must be positive")
        Long brandId,

        @Min(value = 1900, message = "Model year must be after 1900")
        @Max(value = 2100, message = "Model year must be before 2100")
        Integer modelYear,

        @Size(max = 50, message = "Body type must not exceed 50 characters")
        String bodyType
) {
}