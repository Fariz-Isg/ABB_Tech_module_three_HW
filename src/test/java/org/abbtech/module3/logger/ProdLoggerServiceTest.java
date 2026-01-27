package org.abbtech.module3.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProdLoggerService Tests")
class ProdLoggerServiceTest {

    private ProdLoggerService loggerService;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        loggerService = new ProdLoggerService();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("Should log request")
    void testLogRequest() {
        loggerService.logRequest("/cars", "test data");

        String output = outputStream.toString();
        assertTrue(output.contains("PROD"));
        assertTrue(output.contains("REQUEST"));
        assertTrue(output.contains("/cars"));
    }

    @Test
    @DisplayName("Should log response")
    void testLogResponse() {
        loggerService.logResponse("/cars/1", "response data");

        String output = outputStream.toString();
        assertTrue(output.contains("PROD"));
        assertTrue(output.contains("RESPONSE"));
        assertTrue(output.contains("/cars/1"));
    }

    @Test
    @DisplayName("Should log error")
    void testLogError() {
        loggerService.logError("/cars/999", "Car not found");

        String output = outputStream.toString();
        assertTrue(output.contains("PROD"));
        assertTrue(output.contains("ERROR"));
        assertTrue(output.contains("/cars/999"));
        assertTrue(output.contains("Car not found"));
    }
}