package org.abbtech.module3.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DevLoggerService Tests")
class DevLoggerServiceTest {

    private DevLoggerService loggerService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        loggerService = new DevLoggerService();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Should log request")
    void testLogRequest() {
        loggerService.logRequest("/cars", "test data");

        String output = outputStream.toString();
        assertTrue(output.contains("DEV REQUEST"));
        assertTrue(output.contains("/cars"));
        assertTrue(output.contains("test data"));
    }

    @Test
    @DisplayName("Should log response")
    void testLogResponse() {
        loggerService.logResponse("/cars/1", "response data");

        String output = outputStream.toString();
        assertTrue(output.contains("DEV RESPONSE"));
        assertTrue(output.contains("/cars/1"));
        assertTrue(output.contains("response data"));
    }

    @Test
    @DisplayName("Should log error")
    void testLogError() {
        loggerService.logError("/cars/999", "Car not found");

        String output = outputStream.toString();
        assertTrue(output.contains("DEV ERROR"));
        assertTrue(output.contains("/cars/999"));
        assertTrue(output.contains("Car not found"));
    }
}