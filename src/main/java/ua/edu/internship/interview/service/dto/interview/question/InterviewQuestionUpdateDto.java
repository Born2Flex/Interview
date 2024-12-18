package ua.edu.internship.interview.service.dto.interview.question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestionUpdateDto {
    @Min(0)
    @Max(100)
    private Integer grade;
}
