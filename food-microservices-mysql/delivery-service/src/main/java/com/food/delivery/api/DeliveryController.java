package com.food.delivery.api;

import com.food.delivery.domain.Delivery;
import com.food.delivery.repo.DeliveryRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository repo;
    public DeliveryController(DeliveryRepository repo){ this.repo = repo; }

    public record CreateDeliveryReq(@NotBlank String orderId,
                                    @NotBlank String userId,
                                    @NotBlank String address){}

    public record CreateDeliveryRes(Long deliveryId, String status){}

    @PostMapping
    public ResponseEntity<CreateDeliveryRes> create(@RequestBody CreateDeliveryReq req){
        var d = Delivery.builder()
                .orderId(req.orderId()).userId(req.userId())
                .address(req.address())
                .status("SCHEDULED").createdAt(Instant.now())
                .build();
        d = repo.save(d);
        return ResponseEntity.ok(new CreateDeliveryRes(d.getId(), d.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
