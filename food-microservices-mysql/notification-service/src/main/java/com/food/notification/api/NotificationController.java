package com.food.notification.api;

import com.food.notification.domain.Notification;
import com.food.notification.repo.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository repo;
    public NotificationController(NotificationRepository repo){ this.repo = repo; }

    public record SendReq(String userId, String title, String body, String channel){}
    public record SendRes(Long id, String status){}

    @PostMapping
    public ResponseEntity<SendRes> send(@RequestBody SendReq req){
        var n = Notification.builder()
                .userId(req.userId()).title(req.title()).body(req.body())
                .channel(req.channel()==null? "LOG": req.channel())
                .createdAt(Instant.now()).build();
        n = repo.save(n);
        // حاليًا مجرد تخزين؛ تقدر تضيف إرسال حقيقي لاحقًا
        return ResponseEntity.ok(new SendRes(n.getId(), "SENT"));
    }
}
