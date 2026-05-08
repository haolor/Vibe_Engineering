package com.vibe.notification.api;

import com.vibe.notification.store.NotificationStore;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vibe.notification.service.EmailService;
import com.vibe.notification.service.AnomalyService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationStore notificationStore;
    private final EmailService emailService;
    private final AnomalyService anomalyService;

    public NotificationController(NotificationStore notificationStore, EmailService emailService, AnomalyService anomalyService) {
        this.notificationStore = notificationStore;
        this.emailService = emailService;
        this.anomalyService = anomalyService;
    }

    @GetMapping("/")
    public ResponseEntity<?> list(
            @RequestHeader(name = "X-User-Id", required = false) String userId,
            @RequestParam(name = "limit", required = false) Integer limit) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        try {
            List<Map<String, Object>> data = notificationStore.list(userId, limit);
            return ResponseEntity.ok(Map.of("results", data));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/unread_count/")
    public ResponseEntity<?> unreadCount(@RequestHeader(name = "X-User-Id", required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        return ResponseEntity.ok(Map.of("unread_count", notificationStore.unreadCount(userId)));
    }

    @PostMapping("/{id}/mark_read/")
    public ResponseEntity<?> markRead(
            @RequestHeader(name = "X-User-Id", required = false) String userId,
            @PathVariable(name = "id") long id) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        notificationStore.markRead(userId, id);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/mark_all_read/")
    public ResponseEntity<?> markAllRead(@RequestHeader(name = "X-User-Id", required = false) String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }
        notificationStore.markAllRead(userId);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/send-email/")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> payload) {
        String to = payload.get("to");
        String subject = payload.get("subject");
        String content = payload.get("content");
        emailService.sendEmail(to, subject, content);
        return ResponseEntity.ok(Map.of("status", "sent"));
    }

    @PostMapping("/check-anomaly/")
    public ResponseEntity<?> checkAnomaly(@RequestHeader(name = "X-User-Id") String userId, @RequestBody Map<String, Object> transaction) {
        anomalyService.checkAndNotify(userId, transaction);
        return ResponseEntity.ok(Map.of("status", "checked"));
    }
}
