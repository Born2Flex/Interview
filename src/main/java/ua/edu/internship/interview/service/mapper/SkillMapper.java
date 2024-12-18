package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import java.util.List;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = BaseMapper.class)
public interface SkillMapper {
    SkillDto toDto(SkillDocument document);

    List<SkillDto> toDto(List<SkillDocument> documents);
}
