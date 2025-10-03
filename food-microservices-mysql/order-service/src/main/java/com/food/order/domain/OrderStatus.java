package com.food.order.domain;
public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_FAILED,
    FAILED_RESERVATION,   // ← أضف هذه
    CONFIRMED
}
