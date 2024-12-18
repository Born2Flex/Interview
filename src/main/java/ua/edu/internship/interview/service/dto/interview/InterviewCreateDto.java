package ua.edu.internship.interview.service.dto.interview;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InterviewCreateDto extends InterviewBaseDto {

    public InterviewCreateDto(Long interviewerId, Long candidateId, String title, LocalDateTime plannedTime) {
        super(interviewerId, candidateId, title, plannedTime);
    }
}
