package com.vibe.auth.repo;

import com.vibe.auth.model.UserDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUsername(String username);
    boolean existsByUsername(String username);
}
