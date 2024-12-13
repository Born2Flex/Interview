package ua.edu.internship.interview.service.dto.user.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserQuestionBaseDto {
    @NotBlank
    @Size(max = 255)
    private String text;
    @NotBlank
    private String skillId;
    @NotNull
    private QuestionDifficulty difficulty;
    @NotNull
    private QuestionType type;
}
