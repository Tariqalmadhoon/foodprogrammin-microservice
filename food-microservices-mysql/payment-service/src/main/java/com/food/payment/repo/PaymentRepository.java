package com.food.payment.repo;
import com.food.payment.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {}
