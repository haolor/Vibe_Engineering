package com.vibe.notification.store;

import com.vibe.notification.config.SequenceService;
import com.vibe.notification.model.NotificationDocument;
import com.vibe.notification.repo.NotificationRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class NotificationStore {
    private final NotificationRepository notificationRepository;
    private final SequenceService sequenceService;

    public NotificationStore(NotificationRepository notificationRepository, SequenceService sequenceService) {
        this.notificationRepository = notificationRepository;
        this.sequenceService = sequenceService;
    }

    public List<Map<String, Object>> list(String userId, Integer limit) {
        seedIfMissing(userId);
        List<Map<String, Object>> all = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toMap).toList();
        int max = limit == null ? all.size() : Math.min(limit, all.size());
        return all.subList(0, max);
    }

    public int unreadCount(String userId) {
        seedIfMissing(userId);
        return (int) notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public void markRead(String userId, long notificationId) {
        notificationRepository.findByUserIdAndId(userId, notificationId).ifPresent(item -> {
            item.setRead(true);
            notificationRepository.save(item);
        });
    }

    public void markAllRead(String userId) {
        List<NotificationDocument> all = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        all.forEach(item -> item.setRead(true));
        notificationRepository.saveAll(all);
    }

    public void createNotification(String userId, String type, String title, String message, Map<String, Object> metadata) {
        NotificationDocument doc = notification(userId, type, title, message, false);
        doc.setMetadata(metadata);
        notificationRepository.save(doc);
    }

    private void seedIfMissing(String userId) {
        if (!notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).isEmpty()) {
            return;
        }
        notificationRepository.save(notification(userId, "report_ready", "Báo cáo sẵn sàng", "Báo cáo tháng trước đã được tạo", true));
        notificationRepository.save(notification(userId, "system", "Chào mừng", "Hệ thống thông báo đã sẵn sàng", true));
    }

    private NotificationDocument notification(String userId, String type, String title, String message, boolean isRead) {
        NotificationDocument item = new NotificationDocument();
        item.setId(sequenceService.next("notification_id"));
        item.setUserId(userId);
        item.setType(type);
        item.setTitle(title);
        item.setMessage(message);
        item.setCreatedAt(Instant.now().toString());
        item.setRead(isRead);
        return item;
    }

    private Map<String, Object> toMap(NotificationDocument item) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", item.getId());
        map.put("type", item.getType());
        map.put("title", item.getTitle());
        map.put("message", item.getMessage());
        map.put("created_at", item.getCreatedAt());
        map.put("is_read", item.isRead());
        map.put("metadata", item.getMetadata());
        return map;
    }
}
