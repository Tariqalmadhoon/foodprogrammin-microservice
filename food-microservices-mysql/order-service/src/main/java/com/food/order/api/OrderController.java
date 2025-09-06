package com.food.order.api;
import com.food.order.domain.OrderEntity;
import com.food.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
@RestController @RequestMapping("/api/orders") @RequiredArgsConstructor
public class OrderController {
  private final RestClient paymentClient; private final OrderRepository orders; private static final AtomicInteger SEQ=new AtomicInteger(1);
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse create(@Validated @RequestBody CreateOrderRequest req){
    String id="o-"+SEQ.getAndIncrement();
    double total = req.items().stream().mapToInt(i->i.quantity()).sum()*10.0;
    var entity=OrderEntity.builder().id(id).status("PENDING_PAYMENT").total(total).currency("USD").createdAt(Instant.now()).build();
    orders.save(entity);
    Map<String,Object> payResp;
    try{
      payResp = paymentClient.post().uri("/api/payments").contentType(MediaType.APPLICATION_JSON)
        .body(Map.of("orderId",id,"userId",req.userId(),"amount",total,"currency","USD","method",req.paymentMethod()))
        .retrieve().body(new ParameterizedTypeReference<Map<String,Object>>(){});
    }catch(RestClientResponseException ex){
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,"Payment call failed: "+ex.getRawStatusCode()+" "+ex.getResponseBodyAsString(),ex);
    }catch(ResourceAccessException ex){
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,"Cannot reach payment-service at http://localhost:8082",ex);
    }
    String status=String.valueOf(payResp.get("status"));
    entity.setStatus("AUTHORIZED".equalsIgnoreCase(status)?"CONFIRMED":"PAYMENT_FAILED");
    orders.save(entity);
    return new OrderResponse(entity.getId(),entity.getStatus(),entity.getTotal(),entity.getCurrency());
  }
  @GetMapping("/{id}") public OrderResponse get(@PathVariable("id") String id){
    var o=orders.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Order not found"));
    return new OrderResponse(o.getId(),o.getStatus(),o.getTotal(),o.getCurrency());
  }
}
