package ua.edu.internship.interview.service.dto.interview;

import lombok.Data;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewDto {
    private String id;
    private String interviewerId;
    private String candidateId;
    private String title;
    private InterviewStatus status;
    private LocalDateTime plannedTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String feedback;
    private List<InterviewQuestionDto> questions;
}
