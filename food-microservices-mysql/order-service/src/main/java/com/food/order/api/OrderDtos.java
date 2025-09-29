package com.food.order.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class OrderDtos {
    public record CreateOrderRequest(
            @NotBlank String userId,
            @NotBlank String restaurantId,
            @NotEmpty List<Item> items,
            @NotBlank String paymentMethod   // CARD or FAIL
    ) {
        public record Item(@NotBlank String menuItemId, @Positive int quantity) {}
    }

    public record OrderResponse(String orderId, String status, double total, String currency) {}
}
