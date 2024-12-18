package ua.edu.internship.interview.service.dto.interview.question;

import lombok.Builder;
import lombok.Data;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;

@Data
@Builder
public class InterviewQuestionDto {
    private String id;
    private UserQuestionDto question;
    private Integer grade;
}
