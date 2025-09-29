package com.food.restaurant.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        @NotBlank String id,       // m-1
        @NotBlank String name,
        @Positive BigDecimal price
) {}
