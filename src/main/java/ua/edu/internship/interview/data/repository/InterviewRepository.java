package ua.edu.internship.interview.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.edu.internship.interview.data.documents.InterviewDocument;
import java.time.LocalDateTime;
import java.util.List;

public interface InterviewRepository extends MongoRepository<InterviewDocument, String> {
    @Query("{ '$or': [ { 'interviewer_id': ?0 }, { 'candidate_id': ?1 } ], 'start_time': { $gte: ?2, $lte: ?3 } }")
    List<InterviewDocument> findInterviewsInTimeWindow(Long interviewerId,
                                                       Long candidateId,
                                                       LocalDateTime startTime,
                                                       LocalDateTime endTime);
}
