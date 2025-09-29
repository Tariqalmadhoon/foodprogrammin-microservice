package com.food.inventory.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items", indexes = {
        @Index(name = "ux_menu_item_id", columnList = "menuItemId", unique = true)
})
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String menuItemId;

    @Column(nullable = false)
    private int availableQty;

    public InventoryItem() {}

    public InventoryItem(String menuItemId, int availableQty) {
        this.menuItemId = menuItemId;
        this.availableQty = availableQty;
    }

    public Long getId() { return id; }
    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    public int getAvailableQty() { return availableQty; }
    public void setAvailableQty(int availableQty) { this.availableQty = availableQty; }
}
