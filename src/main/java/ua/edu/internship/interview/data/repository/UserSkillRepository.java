package ua.edu.internship.interview.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.edu.internship.interview.data.documents.UserSkillsDocument;
import java.util.Optional;

public interface UserSkillRepository extends MongoRepository<UserSkillsDocument, ObjectId> {
    Optional<UserSkillsDocument> findByUserId(Long userId);
}
