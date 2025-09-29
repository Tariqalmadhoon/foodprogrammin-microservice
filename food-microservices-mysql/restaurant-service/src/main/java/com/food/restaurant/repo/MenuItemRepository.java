package com.food.restaurant.repo;

import com.food.restaurant.domain.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, String> {
}