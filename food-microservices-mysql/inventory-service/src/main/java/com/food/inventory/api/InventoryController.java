package com.food.inventory.api;

import com.food.inventory.domain.InventoryItem;
import com.food.inventory.repo.InventoryItemRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryItemRepository repo;

    public InventoryController(InventoryItemRepository repo) {
        this.repo = repo;
    }

    // -------- DTOs بسيطة ----------
    public record AddStockRequest(
            @NotBlank String menuItemId,
            @Min(1) int quantity
    ) {}

    public record ReserveItem(String menuItemId, @Min(1) int quantity) {}
    public record ReserveRequest(@NotBlank String orderId, List<ReserveItem> items) {}
    public record ReserveResponse(boolean ok, String message) {}

    // 1) زيادة / إنشاء مخزون لعنصر
    @PostMapping("/add")
    public ResponseEntity<ReserveResponse> add(@Valid @RequestBody AddStockRequest req) {
        InventoryItem item = repo.findById(req.menuItemId())
                .orElse(new InventoryItem(req.menuItemId(), 0));
        item.setAvailableQty(item.getAvailableQty() + req.quantity());
        repo.save(item);
        return ResponseEntity.ok(new ReserveResponse(true, "Stock updated for " + req.menuItemId()));
    }

    // 2) حجز مخزون — لو ما بكفي → 409 CONFLICT
    @PostMapping("/reserve")
    @Transactional
    public ResponseEntity<ReserveResponse> reserve(@Valid @RequestBody ReserveRequest req) {
        // تحقق مسبق: كل العناصر متوفرة بكفاية؟
        for (ReserveItem it : req.items()) {
            InventoryItem item = repo.findById(it.menuItemId()).orElse(null);
            if (item == null || item.getAvailableQty() < it.quantity()) {
                String msg = (item == null)
                        ? "Item not found: " + it.menuItemId()
                        : "Insufficient stock for " + it.menuItemId()
                        + " (have " + item.getAvailableQty() + ", need " + it.quantity() + ")";
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ReserveResponse(false, msg));
            }
        }

        // الخصم (بعد التأكد كله يكفي)
        for (ReserveItem it : req.items()) {
            InventoryItem item = repo.findById(it.menuItemId()).orElseThrow();
            item.setAvailableQty(item.getAvailableQty() - it.quantity());
            repo.save(item);
        }

        return ResponseEntity.ok(new ReserveResponse(true, "Reserved for order " + req.orderId()));
    }
}
