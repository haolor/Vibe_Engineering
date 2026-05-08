package com.vibe.notification.repo;

import com.vibe.notification.model.NotificationDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
    List<NotificationDocument> findByUserIdOrderByCreatedAtDesc(String userId);
    long countByUserIdAndIsReadFalse(String userId);
    Optional<NotificationDocument> findByUserIdAndId(String userId, Long id);
}
