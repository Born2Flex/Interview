package ua.edu.internship.interview.service.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @Builder
    public InterviewDto(Long interviewerId, Long candidateId, String title, LocalDateTime plannedTime, String id,
                        InterviewStatus status, LocalDateTime startTime, LocalDateTime endTime, String feedback,
                        List<InterviewQuestionDto> questions) {
        super(interviewerId, candidateId, title, plannedTime);
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.feedback = feedback;
        this.questions = questions;
    }


}
