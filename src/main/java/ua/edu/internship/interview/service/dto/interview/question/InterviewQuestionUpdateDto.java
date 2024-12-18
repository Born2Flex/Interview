package ua.edu.internship.interview.service.dto.interview.question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InterviewQuestionUpdateDto {
    @Min(0)
    @Max(100)
    private Integer grade;
}
