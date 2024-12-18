package ua.edu.internship.interview.service.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
