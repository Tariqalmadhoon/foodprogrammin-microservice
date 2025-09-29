package com.food.delivery.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="deliveries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Delivery {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String orderId;
    @Column(nullable=false) private String userId;
    @Column(nullable=false) private String address;
    @Column(nullable=false) private String status; // SCHEDULED/OUT_FOR_DELIVERY/DELIVERED
    @Column(nullable=false) private Instant createdAt;
}
