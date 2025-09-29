package com.food.inventory.api;

import com.food.inventory.domain.InventoryItem;
import com.food.inventory.repo.InventoryItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.food.inventory.api.ReserveDtos.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryItemRepository repo;

    public InventoryController(InventoryItemRepository repo) {
        this.repo = repo;
    }

    // زيادة / إنشاء مخزون لعنصر
    @PostMapping("/add")
    public ResponseEntity<ReserveResponse> add(@Valid @RequestBody AddStockRequest req) {
        InventoryItem item = repo.findById(req.menuItemId()).orElse(new InventoryItem(req.menuItemId(), 0));
        item.setAvailableQty(item.getAvailableQty() + req.quantity());
        repo.save(item);
        return ResponseEntity.ok(new ReserveResponse(true, "Stock updated for " + req.menuItemId()));
    }
}
