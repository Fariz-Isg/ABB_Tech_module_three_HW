package org.abbtech.module3.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CarErrorEnum Tests")
class CarErrorEnumTest {

    @Test
    @DisplayName("CAR_NOT_FOUND should have correct values")
    void testCarNotFound() {
        assertEquals("CAR-NOT-FOUND-001", CarErrorEnum.CAR_NOT_FOUND.getErrorCode());
        assertEquals("Car not found with given id", CarErrorEnum.CAR_NOT_FOUND.getMessage());
        assertEquals(404, CarErrorEnum.CAR_NOT_FOUND.getHttpStatus());
    }

    @Test
    @DisplayName("INVALID_CAR_DATA should have correct values")
    void testInvalidCarData() {
        assertEquals("INVALID-CAR-DATA-003", CarErrorEnum.INVALID_CAR_DATA.getErrorCode());
        assertEquals("Invalid car data provided", CarErrorEnum.INVALID_CAR_DATA.getMessage());
        assertEquals(400, CarErrorEnum.INVALID_CAR_DATA.getHttpStatus());
    }
}