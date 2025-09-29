package com.food.restaurant.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateMenuItemRequest(
        @NotBlank String id,
        @NotBlank String restaurantId,
        @NotBlank String name,
        @NotNull @Positive BigDecimal price
) {}
