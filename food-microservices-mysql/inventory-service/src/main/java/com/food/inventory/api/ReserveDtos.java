package com.food.inventory.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class ReserveDtos {

    public record ReserveRequest(
            @NotBlank String orderId,
            List<Item> items
    ) {
        public record Item(@NotBlank String menuItemId, @Positive int quantity) {}
    }

    public record ReserveResponse(boolean ok, String message) {}

    public record AddStockRequest(@NotBlank String menuItemId, @Positive int quantity) {}
}
