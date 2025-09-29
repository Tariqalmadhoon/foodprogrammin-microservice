package com.food.inventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @NotBlank
    private String menuItemId;   // يطابق id في المطعم (m-1)

    @PositiveOrZero
    private int availableQty;

    private Instant createdAt = Instant.now();

    public InventoryItem() {}
    public InventoryItem(String menuItemId, int availableQty) {
        this.menuItemId = menuItemId;
        this.availableQty = availableQty;
    }

    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    public int getAvailableQty() { return availableQty; }
    public void setAvailableQty(int availableQty) { this.availableQty = availableQty; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
