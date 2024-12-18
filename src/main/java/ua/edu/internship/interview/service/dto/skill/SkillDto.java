package ua.edu.internship.interview.service.dto.skill;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SkillDto extends SkillBaseDto {
    @Builder
    public SkillDto(String id, String name) {
        super(id, name);
    }
}
