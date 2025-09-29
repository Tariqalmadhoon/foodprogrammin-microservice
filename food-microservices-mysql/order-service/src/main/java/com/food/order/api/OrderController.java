package com.food.order.api;

import com.food.order.api.OrderDtos.CreateOrderRequest;
import com.food.order.api.OrderDtos.OrderResponse;
import com.food.order.domain.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderRepository repo;
  private final RestTemplate http;

  @Value("${payment.base-url:http://localhost:8082}")
  private String paymentBaseUrl;

  public OrderController(OrderRepository repo, RestTemplate http) {
    this.repo = repo;
    this.http = http;
  }

  // أسعار تجريبية
  private static final Map<String, BigDecimal> PRICE = Map.of(
          "m-1", BigDecimal.valueOf(10),
          "m-5", BigDecimal.valueOf(10)
  );

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest req) {
    // احسب المجموع
    BigDecimal total = req.items().stream()
            .map(i -> PRICE.getOrDefault(i.menuItemId(), BigDecimal.TEN).multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    var id = "o-" + UUID.randomUUID().toString().substring(0, 8);

    // احفظ الطلب كـ PENDING
    var order = Order.builder()
            .id(id)
            .status(OrderStatus.PENDING_PAYMENT)
            .total(total)
            .currency("USD")
            .createdAt(LocalDateTime.now())
            .build();
    repo.save(order);

    // نادِ خدمة الدفع
    var paymentReq = Map.of(
            "orderId", id,
            "userId", req.userId(),
            "amount", total,
            "currency", "USD",
            "method", req.paymentMethod()
    );
    record PaymentResp(String paymentId, String orderId, String status) {}

    var resp = http.postForEntity(paymentBaseUrl + "/api/payments", paymentReq, PaymentResp.class);
    var paymentStatus = (resp.getBody() != null) ? resp.getBody().status() : "FAILED";

    // حدّث حالة الطلب
    order.setStatus("AUTHORIZED".equalsIgnoreCase(paymentStatus) ? OrderStatus.CONFIRMED : OrderStatus.PAYMENT_FAILED);
    repo.save(order);

    return ResponseEntity.status(201)
            .body(new OrderResponse(order.getId(), order.getStatus().name(), order.getTotal().doubleValue(), order.getCurrency()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> get(@PathVariable String id) {
    var order = repo.findById(id).orElse(null);
    if (order == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(
            new OrderResponse(order.getId(), order.getStatus().name(), order.getTotal().doubleValue(), order.getCurrency())
    );
  }
}
