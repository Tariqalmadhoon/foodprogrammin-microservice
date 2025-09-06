package com.food.payment.api;
public record CreatePaymentRequest(String orderId,String userId,double amount,String currency,String method) {}
