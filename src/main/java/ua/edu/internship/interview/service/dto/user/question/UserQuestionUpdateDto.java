package ua.edu.internship.interview.service.dto.user.question;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserQuestionUpdateDto extends UserQuestionBaseDto {
    @Builder
    public UserQuestionUpdateDto(String text, String skillId, QuestionDifficulty difficulty, QuestionType type) {
        super(text, skillId, difficulty, type);
    }
}
