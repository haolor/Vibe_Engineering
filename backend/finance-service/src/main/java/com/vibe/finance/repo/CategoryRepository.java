package com.vibe.finance.repo;

import com.vibe.finance.model.CategoryDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<CategoryDocument, String> {
    Optional<CategoryDocument> findById(Long id);
    boolean existsById(Long id);
}
