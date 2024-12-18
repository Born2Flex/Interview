package ua.edu.internship.interview.data.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
@Document
public class InterviewQuestionDocument {
    @Id
    @Field("id")
    private ObjectId id;
    @Field("question")
    private UserQuestionDocument question;
    @Field("grade")
    private Integer grade;
}
