package org.abbtech.module3.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long paymentId,
        String status,
        BigDecimal balance,
        Long userId,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
