package org.abbtech.module3.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CarException Tests")
class CarExceptionTest {

    @Test
    @DisplayName("Should create exception with error service")
    void testConstructorWithErrorService() {
        CarException exception = new CarException(CarErrorEnum.CAR_NOT_FOUND, 123L);

        assertEquals(CarErrorEnum.CAR_NOT_FOUND, exception.baseErrorService);
        assertEquals("Car not found with given id", exception.getMessage());
        assertNotNull(exception.args);
        assertEquals(1, exception.args.length);
        assertEquals(123L, exception.args[0]);
    }

    @Test
    @DisplayName("Should create exception with throwable")
    void testConstructorWithThrowable() {
        Throwable cause = new RuntimeException("Test cause");
        CarException exception = new CarException(CarErrorEnum.INVALID_CAR_DATA, cause, "test arg");

        assertEquals(CarErrorEnum.INVALID_CAR_DATA, exception.baseErrorService);
        assertEquals(cause, exception.getCause());
        assertEquals(1, exception.args.length);
        assertEquals("test arg", exception.args[0]);
    }
}