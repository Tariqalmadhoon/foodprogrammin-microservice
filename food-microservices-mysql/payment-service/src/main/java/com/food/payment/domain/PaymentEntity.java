package com.food.payment.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name="payments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentEntity {
  @Id private String id;
  @Column(nullable=false) private String orderId;
  @Column(nullable=false) private String userId;
  @Column(nullable=false) private double amount;
  @Column(nullable=false, length=8) private String currency;
  @Column(nullable=false, length=16) private String method;
  @Column(nullable=false, length=16) private String status;
  @Column(nullable=false, updatable=false) private Instant createdAt;
}
