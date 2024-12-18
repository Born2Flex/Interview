package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.edu.internship.interview.data.documents.InterviewQuestionDocument;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionUpdateDto;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {BaseMapper.class, UserQuestionMapper.class})
public interface InterviewQuestionMapper {
    InterviewQuestionDto toDto(InterviewQuestionDocument document);

    InterviewQuestionDocument updateDocument(@MappingTarget InterviewQuestionDocument document, InterviewQuestionUpdateDto dto);
}
