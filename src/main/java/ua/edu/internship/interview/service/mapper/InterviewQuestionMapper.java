package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.edu.internship.interview.data.documents.InterviewQuestionDocument;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionCreateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionUpdateDto;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = UserQuestionMapper.class)
public interface InterviewQuestionMapper {
    InterviewQuestionDocument toDocument(InterviewQuestionCreateDto dto);

    InterviewQuestionDto toDto(InterviewQuestionDocument document);

    void updateDocument(InterviewQuestionUpdateDto dto, @MappingTarget InterviewQuestionDocument document);
}
