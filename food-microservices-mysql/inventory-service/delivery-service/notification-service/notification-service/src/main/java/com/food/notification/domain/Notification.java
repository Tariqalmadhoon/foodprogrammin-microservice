package com.food.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String userId;
    @Column(nullable=false) private String title;
    @Column(nullable=false, length=1000) private String body;
    @Column(nullable=false) private String channel; // EMAIL/SMS/PUSH/LOG
    @Column(nullable=false) private Instant createdAt;
}
