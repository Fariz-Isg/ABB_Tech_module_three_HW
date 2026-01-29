package org.abbtech.module3.controller;

import org.abbtech.module3.dto.PaymentRequest;
import org.abbtech.module3.dto.PaymentResponse;
import org.abbtech.module3.exception.InsufficientBalanceException;
import org.abbtech.module3.exception.UserNotFoundException;
import org.abbtech.module3.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void createPayment_Success() throws Exception {
        // Given
        PaymentResponse response = new PaymentResponse(
                1L, "SUCCESS", new BigDecimal("950.00"), null, null, null
        );
        when(paymentService.createPayment(any(PaymentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 101, \"amount\": 50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.balance").value(950.00));
    }

    @Test
    void createPayment_UserNotFound() throws Exception {
        // Given
        when(paymentService.createPayment(any(PaymentRequest.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 999, \"amount\": 50}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"));
    }

    @Test
    void createPayment_InsufficientBalance() throws Exception {
        // Given
        when(paymentService.createPayment(any(PaymentRequest.class)))
                .thenThrow(new InsufficientBalanceException("User balance is not enough"));

        // When & Then
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 101, \"amount\": 1500}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INSUFFICIENT_BALANCE"));
    }

    @Test
    void getAllPayments_Success() throws Exception {
        // Given
        List<PaymentResponse> responses = Arrays.asList(
                new PaymentResponse(1L, "SUCCESS", null, 101L, new BigDecimal("50"), null)
        );
        when(paymentService.getAllPayments()).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(1))
                .andExpect(jsonPath("$[0].userId").value(101));
    }

    @Test
    void getPaymentsByUserId_Success() throws Exception {
        // Given
        List<PaymentResponse> responses = Arrays.asList(
                new PaymentResponse(1L, "SUCCESS", null, null, new BigDecimal("50"), null)
        );
        when(paymentService.getPaymentsByUserId(101L)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/payments/user/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(1));
    }

    @Test
    void getPaymentById_Success() throws Exception {
        // Given
        PaymentResponse response = new PaymentResponse(
                1L, "SUCCESS", null, 101L, new BigDecimal("50"), null
        );
        when(paymentService.getPaymentById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.userId").value(101));
    }
}