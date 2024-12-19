package ua.edu.internship.interview.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends MongoRepository<UserQuestionDocument, ObjectId> {
    List<UserQuestionDocument> findAllByUserId(Long userId);

    List<UserQuestionDocument> findAllByUserIdAndSkillId(Long userId, ObjectId skillId);

    Optional<UserQuestionDocument> findByIdAndUserId(ObjectId id, Long userId);

    void deleteByUserIdAndSkillId(Long userId, ObjectId skillId);
}
