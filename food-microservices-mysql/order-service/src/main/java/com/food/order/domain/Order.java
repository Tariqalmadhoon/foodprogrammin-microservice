package com.food.order.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id
    private String id;  // o-...

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(precision = 19, scale = 2)
    private BigDecimal total;

    private String currency;

    private LocalDateTime createdAt;
}
