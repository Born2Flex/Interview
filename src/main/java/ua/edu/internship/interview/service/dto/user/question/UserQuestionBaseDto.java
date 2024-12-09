package ua.edu.internship.interview.service.dto.user.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserQuestionBaseDto {
    @NotBlank
    private String text;
    @NotBlank
    private String skillId;
    private QuestionDifficulty difficulty;
    private QuestionType type;
}
