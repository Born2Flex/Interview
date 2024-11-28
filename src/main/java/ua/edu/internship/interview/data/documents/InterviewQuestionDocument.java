package ua.edu.internship.interview.data.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@Document
public class InterviewQuestionDocument {
    @Id
    @EqualsAndHashCode.Include
    @Field("id")
    private String id;
    @DocumentReference
    @Field("question")
    private UserQuestionDocument question;
    @Field("grade")
    private Integer grade;
}
