package com.food.order.domain;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
@Entity @Table(name="orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEntity {
  @Id private String id;
  @Column(nullable=false, length=32) private String status;
  @Column(nullable=false) private double total;
  @Column(nullable=false, length=8) private String currency;
  @Column(nullable=false, updatable=false) private Instant createdAt;
}
