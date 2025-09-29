package com.food.restaurant.api;

import java.math.BigDecimal;

public record MenuItemResponse(
        String id,
        String name,
        BigDecimal price
) {}
