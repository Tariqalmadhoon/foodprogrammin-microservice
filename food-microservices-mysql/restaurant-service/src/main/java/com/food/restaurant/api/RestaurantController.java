package com.food.restaurant.api;

import com.food.restaurant.domain.MenuItem;
import com.food.restaurant.repo.MenuItemRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final MenuItemRepository repo;

    public RestaurantController(MenuItemRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/menu-items")
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody CreateMenuItemRequest req) {
        MenuItem item = new MenuItem(req.id(), req.name(), req.price());
        repo.save(item);
        return ResponseEntity.ok(new MenuItemResponse(item.getId(), item.getName(), item.getPrice()));
    }

    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> get(@PathVariable String id) {
        return repo.findById(id)
                .map(m -> ResponseEntity.ok(new MenuItemResponse(m.getId(), m.getName(), m.getPrice())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menu-items")
    public List<MenuItemResponse> list() {
        return repo.findAll().stream()
                .map(m -> new MenuItemResponse(m.getId(), m.getName(), m.getPrice()))
                .toList();
    }
}
