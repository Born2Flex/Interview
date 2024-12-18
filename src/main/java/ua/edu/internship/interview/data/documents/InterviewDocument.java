package ua.edu.internship.interview.data.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
@ToString
@Document(collection = "interviews")
public class InterviewDocument {
    @Id
    @Field(name = "id")
    private ObjectId id;
    @Field(name = "interviewerId")
    private Long interviewerId;
    @Field(name = "candidateId")
    private Long candidateId;
    @Field(name = "title")
    private String title;
    @Field(name = "status")
    private InterviewStatus status;
    @Field(name = "plannedTime")
    private LocalDateTime plannedTime;
    @Field(name = "startTime")
    private LocalDateTime startTime;
    @Field(name = "endTime")
    private LocalDateTime endTime;
    @Field(name = "feedback")
    private String feedback;
    @Field(name = "questions")
    private List<InterviewQuestionDocument> questions = new ArrayList<>();

    public void addQuestion(InterviewQuestionDocument question) {
        questions.add(question);
    }
}
