package ua.edu.internship.interview.service.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InterviewUpdateDto {
    @NotBlank
    private String title;
    @Future
    private LocalDateTime plannedTime;
}
