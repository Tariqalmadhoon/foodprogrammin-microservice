package com.food.order.api;

import com.food.order.api.OrderDtos.CreateOrderRequest;
import com.food.order.api.OrderDtos.OrderResponse;
import com.food.order.domain.Order;
import com.food.order.domain.OrderRepository;
import com.food.order.domain.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderRepository repo;
  private final RestTemplate http;

  @Value("${payment.base-url:http://localhost:8082}")
  private String paymentBaseUrl;

  @Value("${inventory.base-url:http://localhost:8083}")
  private String inventoryBaseUrl;

  // (اختياري) لو بدك تستدعيهم بعد التأكيد
  @Value("${delivery.base-url:http://localhost:8085}")
  private String deliveryBaseUrl;

  @Value("${notification.base-url:http://localhost:8084}")
  private String notificationBaseUrl;

  public OrderController(OrderRepository repo, RestTemplate http) {
    this.repo = repo;
    this.http = http;
  }

  // أسعار تجريبية
  private static final Map<String, BigDecimal> PRICE = Map.of(
          "m-1", BigDecimal.valueOf(10),
          "m-5", BigDecimal.valueOf(10)
  );

  // ====== DTOs خفيفة للنداءات البينية ======
  record PaymentResp(String paymentId, String orderId, String status) {}

  record InventoryItemReq(String menuItemId, int quantity) {}
  record InventoryReserveReq(String orderId, List<InventoryItemReq> items) {}
  record InventoryReserveResp(boolean ok, String message) {}

  @PostMapping
  public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest req) {
    // 1) احسب المجموع
    BigDecimal total = req.items().stream()
            .map(i -> PRICE.getOrDefault(i.menuItemId(), BigDecimal.TEN).multiply(BigDecimal.valueOf(i.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    String id = "o-" + UUID.randomUUID().toString().substring(0, 8);

    // 2) خزّن الطلب كـ PENDING_PAYMENT
    var order = Order.builder()
            .id(id)
            .status(OrderStatus.PENDING_PAYMENT)
            .total(total)
            .currency("USD")
            .createdAt(LocalDateTime.now())
            .build();
    repo.save(order);

    // 3) الدفع
    var paymentReq = Map.of(
            "orderId", id,
            "userId",  req.userId(),
            "amount",  total,
            "currency","USD",
            "method",  req.paymentMethod()
    );

    ResponseEntity<PaymentResp> payResp = http.postForEntity(
            paymentBaseUrl + "/api/payments", paymentReq, PaymentResp.class
    );
    String paymentStatus = (payResp.getBody() != null) ? payResp.getBody().status() : "FAILED";

    if (!"AUTHORIZED".equalsIgnoreCase(paymentStatus)) {
      order.setStatus(OrderStatus.PAYMENT_FAILED);
      repo.save(order);
      return createdResponse(order);
    }

    // 4) حجز المخزون — فشل الحجز يجب أن يوقف التدفق ويعلّم الطلب FAILED_RESERVATION
    var invReq = new InventoryReserveReq(
            id,
            req.items().stream()
                    .map(i -> new InventoryItemReq(i.menuItemId(), i.quantity()))
                    .toList()
    );

    try {
      ResponseEntity<InventoryReserveResp> invResp = http.postForEntity(
              inventoryBaseUrl + "/api/inventory/reserve", invReq, InventoryReserveResp.class
      );

      InventoryReserveResp body = invResp.getBody();
      boolean ok = (body != null && body.ok());

      if (!ok) {
        order.setStatus(OrderStatus.FAILED_RESERVATION);
        repo.save(order);
        return createdResponse(order);
      }

    } catch (HttpClientErrorException e) {
      // لو رجع 409 CONFLICT من خدمة المخزون → نعتبرها فشل حجز
      if (e.getStatusCode() == HttpStatus.CONFLICT) {
        order.setStatus(OrderStatus.FAILED_RESERVATION);
        repo.save(order);
        return createdResponse(order);
      }
      // أي خطأ آخر: نعيد رميه أو تتعامل حسب رغبتك
      throw e;
    }

    // 5) (اختياري) جدول تسليم وإرسال إشعار
    try {
      http.postForEntity(
              deliveryBaseUrl + "/api/deliveries",
              Map.of("orderId", id, "userId", req.userId()),
              Void.class
      );
    } catch (Exception ignore) {}

    try {
      http.postForEntity(
              notificationBaseUrl + "/api/notifications",
              Map.of(
                      "title",   "Order Confirmed",
                      "body",    "Your order " + id + " has been confirmed.",
                      "channel", "EMAIL",
                      "userId",  req.userId()
              ),
              Void.class
      );
    } catch (Exception ignore) {}

    // 6) أكّد الطلب
    order.setStatus(OrderStatus.CONFIRMED);
    repo.save(order);
    return createdResponse(order);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> get(@PathVariable String id) {
    var order = repo.findById(id).orElse(null);
    if (order == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(new OrderResponse(
            order.getId(), order.getStatus().name(), order.getTotal().doubleValue(), order.getCurrency()
    ));
  }

  private ResponseEntity<OrderResponse> createdResponse(Order order) {
    return ResponseEntity.status(201).body(
            new OrderResponse(order.getId(), order.getStatus().name(), order.getTotal().doubleValue(), order.getCurrency())
    );
  }
}
