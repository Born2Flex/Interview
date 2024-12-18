package ua.edu.internship.interview.service.dto.interview.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InterviewQuestionCreateDto {
    @NotBlank
    private String userQuestionId;
}
