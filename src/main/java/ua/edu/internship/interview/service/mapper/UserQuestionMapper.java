package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionCreateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import java.util.List;

@Mapper(componentModel = SPRING, uses = {SkillMapper.class, BaseMapper.class})
public interface UserQuestionMapper {
    @Mapping(target = "skillId", source = "skill.id")
    UserQuestionDto toDto(UserQuestionDocument document);

    List<UserQuestionDto> toDto(List<UserQuestionDocument> documents);

    @Mapping(target = "userId", source = "userId")
    UserQuestionDocument toDocument(Long userId, UserQuestionCreateDto dto);

    UserQuestionDocument updateDocument(@MappingTarget UserQuestionDocument document, UserQuestionUpdateDto dto);
}
