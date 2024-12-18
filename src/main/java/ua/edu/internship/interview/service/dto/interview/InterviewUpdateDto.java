package ua.edu.internship.interview.service.dto.interview;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewUpdateDto {
    @NotBlank
    private String title;
    @Future
    private LocalDateTime plannedTime;
}
