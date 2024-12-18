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
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
@Document(collection = "user_skills")
public class UserSkillsDocument {
    @Id
    @Field(name = "id")
    private ObjectId id;
    @Field(name = "userId")
    private Long userId;
    @Field(name = "skillIds")
    @DocumentReference
    private List<SkillDocument> skills;
}
