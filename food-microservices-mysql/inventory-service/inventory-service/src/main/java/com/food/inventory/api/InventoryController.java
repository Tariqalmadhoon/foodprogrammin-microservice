package com.food.inventory.api;

import com.food.inventory.domain.InventoryItem;
import com.food.inventory.repo.InventoryItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.food.inventory.api.ReserveDtos.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryItemRepository repo;

    public InventoryController(InventoryItemRepository repo) {
        this.repo = repo;
    }

    // حجز مخزون لطلب معيّن
    @PostMapping("/reserve")
    @Transactional
    public ResponseEntity<ReserveResponse> reserve(@RequestBody @Valid ReserveRequest req) {
        List<InventoryItem> changed = new ArrayList<>();

        if (req.items() == null || req.items().isEmpty()) {
            return ResponseEntity.ok(new ReserveResponse(false, "Items list is empty"));
        }

        for (ReserveRequest.Item it : req.items()) {
            var item = repo.findByMenuItemId(it.menuItemId()).orElse(null);
            if (item == null) {
                return ResponseEntity.ok(new ReserveResponse(false, "Item not found: " + it.menuItemId()));
            }
            if (item.getAvailableQty() < it.quantity()) {
                return ResponseEntity.ok(new ReserveResponse(false,
                        "Insufficient stock for " + it.menuItemId()));
            }
            // احجز (نزّل الكمية مؤقتاً)
            item.setAvailableQty(item.getAvailableQty() - it.quantity());
            changed.add(item);
        }

        // حفظ كل التغييرات مرة واحدة
        repo.saveAll(changed);
        return ResponseEntity.ok(new ReserveResponse(true, "Reserved for order " + req.orderId()));
    }

    // إنشاء/تحديث صنف في المخزون (للتهيئة والاختبارات)
    @PostMapping("/items")
    public ResponseEntity<?> upsertItem(@RequestBody @Valid UpsertItemRequest body) {
        var existing = repo.findByMenuItemId(body.menuItemId()).orElse(null);
        if (existing == null) {
            repo.save(new InventoryItem(body.menuItemId(), body.quantity()));
            return ResponseEntity.ok().build();
        }
        existing.setAvailableQty(body.quantity());
        repo.save(existing);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/items/{menuItemId}")
    public ResponseEntity<?> getOne(@PathVariable String menuItemId) {
        return repo.findByMenuItemId(menuItemId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/items")
    public List<InventoryItem> list() {
        return repo.findAll();
    }
}
