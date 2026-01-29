package org.abbtech.module3.dto;

import java.math.BigDecimal;

public record PaymentRequest(Long userId, BigDecimal amount) {
}
