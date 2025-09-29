package com.food.restaurant.api;

import com.food.restaurant.domain.MenuItem;
import com.food.restaurant.repo.MenuItemRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RestaurantController {

    private final MenuItemRepository menuRepo;

    // إضافة صنف للقائمة
    @PostMapping("/menu")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItem create(@Valid @RequestBody CreateMenuItemRequest r) {
        MenuItem item = MenuItem.builder()
                .id(r.id())
                .restaurantId(r.restaurantId())
                .name(r.name())
                .price(r.price())
                .available(true)
                .createdAt(LocalDateTime.now())
                .build();
        return menuRepo.save(item);
    }

    // قائمة مطعم محدد
    @GetMapping("/restaurants/{restaurantId}/menu")
    public List<MenuItem> getMenu(@PathVariable String restaurantId) {
        return menuRepo.findByRestaurantId(restaurantId);
    }

    // صنف واحد (اختياري)
    @GetMapping("/menu/{id}")
    public MenuItem getOne(@PathVariable String id) {
        return menuRepo.findById(id).orElse(null);
    }
}
