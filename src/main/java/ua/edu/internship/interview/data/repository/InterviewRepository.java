package ua.edu.internship.interview.data.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.edu.internship.interview.data.documents.InterviewDocument;

import java.time.LocalDateTime;

public interface InterviewRepository extends MongoRepository<InterviewDocument, ObjectId> {
    @Query(value = "{ '$or': [ { 'interviewer_id': ?0 }, { 'candidate_id': ?1 } ], 'start_time': { $gte: ?2, $lte: ?3 } }", exists = true)
    boolean existsInterviewsInTimeWindow(Long interviewerId,
                                         Long candidateId,
                                         LocalDateTime startTime,
                                         LocalDateTime endTime);
}
