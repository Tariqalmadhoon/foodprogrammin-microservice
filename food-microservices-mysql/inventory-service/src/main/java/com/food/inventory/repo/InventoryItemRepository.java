package com.food.inventory.repo;

import com.food.inventory.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    // المفتاح هو menuItemId
}
