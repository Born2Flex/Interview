package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import ua.edu.internship.interview.data.documents.UserSkillsDocument;
import ua.edu.internship.interview.service.dto.user.skill.UserSkillsDto;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {SkillMapper.class, BaseMapper.class})
public interface UserSkillsMapper {
    UserSkillsDto toDto(UserSkillsDocument document);
}
