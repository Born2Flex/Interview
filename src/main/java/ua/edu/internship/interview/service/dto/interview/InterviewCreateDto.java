package ua.edu.internship.interview.service.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewCreateDto {
    @NotBlank
    private String interviewerId;
    @NotBlank
    private String candidateId;
    @NotBlank
    private String title;
    @Future
    private LocalDateTime plannedTime;
}
