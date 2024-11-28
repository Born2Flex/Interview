package ua.edu.internship.interview.service.dto.interview.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InterviewQuestionCreateDto {
    @NotBlank
    private String userQuestionId;
}
