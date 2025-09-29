package com.food.inventory.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class ReserveDtos {

    // طلب الحجز
    public record ReserveRequest(
            @NotBlank String orderId,
            List<Item> items
    ) {
        public record Item(@NotBlank String menuItemId, @Positive int quantity) {}
    }

    // ردّ الحجز
    public record ReserveResponse(boolean ok, String message) {}

    // لإنشاء/تحديث صنف في المخزون (للتجهيز والاختبار)
    public record UpsertItemRequest(@NotBlank String menuItemId, @Positive int quantity) {}
}
