package com.food.order.api;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
public record CreateOrderRequest(
  @NotBlank String userId,
  @NotBlank String restaurantId,
  @NotEmpty List<Item> items,
  @NotBlank String paymentMethod
){
  public record Item(@NotBlank String menuItemId, @Positive int quantity) {}
}
