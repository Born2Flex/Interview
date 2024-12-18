package ua.edu.internship.interview.service.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InterviewDto extends InterviewBaseDto {
    private String id;
    private InterviewStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String feedback;
    private List<InterviewQuestionDto> questions;
}
