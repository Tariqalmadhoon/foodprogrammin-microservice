package com.food.payment.api;
import com.food.payment.domain.PaymentEntity;
import com.food.payment.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestController @RequestMapping("/api/payments") @RequiredArgsConstructor
public class PaymentController {
  private final PaymentRepository payments;
  @PostMapping public Map<String,Object> pay(@RequestBody CreatePaymentRequest req){
    String id="p-"+System.currentTimeMillis();
    String status = "CARD".equalsIgnoreCase(req.method()) ? "AUTHORIZED" : "FAILED";
    var entity=PaymentEntity.builder().id(id).orderId(req.orderId()).userId(req.userId()).amount(req.amount()).currency(req.currency()).method(req.method()).status(status).createdAt(Instant.now()).build();
    payments.save(entity);
    return Map.of("paymentId",id,"orderId",req.orderId(),"status",status);
  }
}
