package org.abbtech.module3.exception;

import org.abbtech.module3.exception.base.BaseErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getContextPath()).thenReturn("/api");
    }

    @Test
    @DisplayName("Should handle CarException")
    void testHandleCarException() {
        CarException exception = new CarException(CarErrorEnum.CAR_NOT_FOUND, 123L);

        ResponseEntity<BaseErrorResponseDTO> response = handler.handleCarException(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("CAR-NOT-FOUND-001", response.getBody().code());
        assertEquals(404, response.getBody().status());
    }

    @Test
    @DisplayName("Should handle validation exception")
    void testHandleValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("car", "name", "must not be null");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<BaseErrorResponseDTO> response = handler.handleBaseException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BASE-VALIDATION-ERROR-001", response.getBody().code());
    }
}