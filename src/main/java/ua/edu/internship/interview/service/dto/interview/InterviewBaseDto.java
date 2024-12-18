package ua.edu.internship.interview.service.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewBaseDto {
    @NotNull
    private Long interviewerId;
    @NotNull
    private Long candidateId;
    @NotBlank
    private String title;
    @Future
    private LocalDateTime plannedTime;
}
