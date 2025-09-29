package com.food.restaurant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "menu_items")
public class MenuItem {

    @Id
    private String id;             // مثل m-1

    @NotBlank
    private String name;

    @Positive
    private BigDecimal price;

    private Instant createdAt = Instant.now();

    // getters/setters/constructors
    public MenuItem() {}
    public MenuItem(String id, String name, BigDecimal price) {
        this.id = id; this.name = name; this.price = price;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
