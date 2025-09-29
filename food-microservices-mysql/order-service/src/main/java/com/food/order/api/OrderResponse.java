package com.food.order.api;
public record OrderResponse(String orderId, String status, double total, String currency) {}
