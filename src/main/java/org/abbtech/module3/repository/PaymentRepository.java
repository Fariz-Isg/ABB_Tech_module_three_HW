package org.abbtech.module3.repository;

import org.abbtech.module3.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByUserIdOrderByCreatedAtDesc(Long id);

    List<Payment> findAllByOrderByCreatedAtDesc();
}
