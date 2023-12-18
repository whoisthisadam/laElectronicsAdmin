package com.kasperovich.laelectronics.repository;

import com.kasperovich.laelectronics.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
