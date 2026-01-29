package org.abbtech.module3.service;

import org.abbtech.module3.dto.PaymentRequest;
import org.abbtech.module3.dto.PaymentResponse;
import org.abbtech.module3.model.Payment;
import org.abbtech.module3.model.User;
import org.abbtech.module3.exception.InsufficientBalanceException;
import org.abbtech.module3.exception.PaymentNotFoundException;
import org.abbtech.module3.exception.UserNotFoundException;
import org.abbtech.module3.repository.PaymentRepository;
import org.abbtech.module3.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User testUser;
    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(101L)
                .fullName("John Doe")
                .balance(new BigDecimal("1000.00"))
                .build();

        testPayment = Payment.builder()
                .id(1L)
                .userId(101L)
                .amount(new BigDecimal("50.00"))
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createPayment_Success() {
        PaymentRequest request = new PaymentRequest(101L, new BigDecimal("50.00"));
        when(userRepository.findById(101L)).thenReturn(Optional.of(testUser));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        PaymentResponse response = paymentService.createPayment(request);

        assertNotNull(response);
        assertEquals(1L, response.paymentId());
        assertEquals("SUCCESS", response.status());
        assertEquals(new BigDecimal("950.00"), response.balance());

        verify(userRepository).findById(101L);
        verify(paymentRepository, times(2)).save(any(Payment.class)); // PENDING + SUCCESS
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createPayment_UserNotFound() {
        PaymentRequest request = new PaymentRequest(999L, new BigDecimal("50.00"));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                paymentService.createPayment(request)
        );

        verify(userRepository).findById(999L);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void createPayment_InsufficientBalance() {
        PaymentRequest request = new PaymentRequest(101L, new BigDecimal("1500.00"));
        when(userRepository.findById(101L)).thenReturn(Optional.of(testUser));

        assertThrows(InsufficientBalanceException.class, () ->
                paymentService.createPayment(request)
        );

        verify(userRepository).findById(101L);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void getAllPayments_Success() {
        List<Payment> payments = Arrays.asList(testPayment);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<PaymentResponse> responses = paymentService.getAllPayments();

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1L, responses.get(0).paymentId());
        assertEquals(101L, responses.get(0).userId());
    }

    @Test
    void getPaymentsByUserId_Success() {
        when(userRepository.findById(101L)).thenReturn(Optional.of(testUser));
        when(paymentRepository.findByUserIdOrderByCreatedAtDesc(101L))
                .thenReturn(Arrays.asList(testPayment));

        List<PaymentResponse> responses = paymentService.getPaymentsByUserId(101L);

        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(userRepository).findById(101L);
        verify(paymentRepository).findByUserIdOrderByCreatedAtDesc(101L);
    }

    @Test
    void getPaymentsByUserId_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                paymentService.getPaymentsByUserId(999L)
        );

        verify(userRepository).findById(999L);
        verify(paymentRepository, never()).findByUserIdOrderByCreatedAtDesc(any());
    }

    @Test
    void getPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        PaymentResponse response = paymentService.getPaymentById(1L);

        assertNotNull(response);
        assertEquals(1L, response.paymentId());
        assertEquals(101L, response.userId());
        assertEquals("SUCCESS", response.status());

        verify(paymentRepository).findById(1L);
    }

    @Test
    void getPaymentById_PaymentNotFound() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () ->
                paymentService.getPaymentById(999L)
        );

        verify(paymentRepository).findById(999L);
    }
}