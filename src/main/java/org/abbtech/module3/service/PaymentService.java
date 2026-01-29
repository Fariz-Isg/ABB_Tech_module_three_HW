package org.abbtech.module3.service;

import lombok.RequiredArgsConstructor;
import org.abbtech.module3.dto.PaymentRequest;
import org.abbtech.module3.dto.PaymentResponse;
import org.abbtech.module3.exception.InsufficientBalanceException;
import org.abbtech.module3.exception.PaymentNotFoundException;
import org.abbtech.module3.exception.UserNotFoundException;
import org.abbtech.module3.model.Payment;
import org.abbtech.module3.model.User;
import org.abbtech.module3.repository.PaymentRepository;
import org.abbtech.module3.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        BigDecimal newBalance = user.getBalance().subtract(request.amount());
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBalanceException("User balance is not enough");
        }

        Payment payment = Payment.builder()
                .userId(request.userId())
                .amount(request.amount())
                .status("PENDING")
                .build();

        payment = paymentRepository.save(payment);

        user.setBalance(newBalance);
        userRepository.save(user);

        payment.setStatus("SUCCESS");
        payment =  paymentRepository.save(payment);

        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                newBalance,
                null,
                null,
                null
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(payment -> new PaymentResponse(
                        payment.getId(),
                        payment.getStatus(),
                        null,
                        payment.getUserId(),
                        payment.getAmount(),
                        payment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Payment> payments = paymentRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return payments.stream()
                .map(payment -> new PaymentResponse(
                        payment.getId(),
                        payment.getStatus(),
                        null,
                        null,
                        payment.getAmount(),
                        payment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                null,
                payment.getUserId(),
                payment.getAmount(),
                payment.getCreatedAt()
        );
    }
}
