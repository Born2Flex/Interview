package ua.edu.internship.interview.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ua.edu.internship.interview.data.documents.InterviewDocument;
import ua.edu.internship.interview.service.dto.interview.InterviewCreateDto;
import ua.edu.internship.interview.service.dto.interview.InterviewDto;
import ua.edu.internship.interview.service.dto.interview.InterviewUpdateDto;
import java.util.List;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {BaseMapper.class, InterviewQuestionMapper.class})
public interface InterviewMapper {
    InterviewDto toDto(InterviewDocument document);

    List<InterviewDto> toDto(List<InterviewDocument> document);

    InterviewDocument toDocument(InterviewCreateDto dto);

    InterviewDocument updateDocument(@MappingTarget InterviewDocument document, InterviewUpdateDto dto);
}
