package ua.edu.internship.interview.service.dto.user.question;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;
import ua.edu.internship.interview.service.dto.skill.SkillDto;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserQuestionDto extends UserQuestionBaseDto {
    private String id;
    private Long userId;
    private SkillDto skill;

    @Builder
    public UserQuestionDto(String id, Long userId, SkillDto skill, String text,
                           String skillId, QuestionDifficulty difficulty, QuestionType type) {
        super(text, skillId, difficulty, type);
        this.id = id;
        this.userId = userId;
        this.skill = skill;
    }
}
