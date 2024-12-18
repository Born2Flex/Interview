package ua.edu.internship.interview.service.dto.user.skill;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import java.util.List;

@Data
@NoArgsConstructor
public class UserSkillsDto {
    private String id;
    private Long userId;
    private List<SkillDto> skills;

    @Builder
    public UserSkillsDto(String id, Long userId, List<SkillDto> skills) {
        this.id = id;
        this.userId = userId;
        this.skills = skills;
    }
}
