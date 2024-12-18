package ua.edu.internship.interview.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import ua.edu.internship.interview.service.business.InterviewService;
import ua.edu.internship.interview.service.dto.interview.InterviewCreateDto;
import ua.edu.internship.interview.service.dto.interview.InterviewDto;
import ua.edu.internship.interview.service.dto.interview.InterviewUpdateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionCreateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionUpdateDto;
import java.util.List;

@Tag(name = "Interviews", description = "Interviews management endpoints")
@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService service;

    @GetMapping
    public List<InterviewDto> getAllInterviews() {
        return service.getAllInterviews();
    }

    @GetMapping("/{interviewId}")
    public InterviewDto getInterviewById(@PathVariable String interviewId) {
        return service.getInterviewById(interviewId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewDto createInterview(@RequestBody @Valid InterviewCreateDto dto) {
        return service.createInterview(dto);
    }

    @PutMapping("/{interviewId}")
    public InterviewDto updateInterview(@PathVariable String interviewId, @RequestBody @Valid InterviewUpdateDto dto) {
        return service.updateInterview(interviewId, dto);
    }

    @DeleteMapping("/{interviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelInterview(@PathVariable String interviewId) {
        service.deleteInterviewById(interviewId);
    }

//TODO Add comment in pull request, so it would be reasonable to add this endpoint
    @PatchMapping("/{interviewId}/status")
    public InterviewDto updateInterviewStatus(@PathVariable String interviewId, @RequestBody InterviewStatus status) {
        return service.updateInterviewStatus(interviewId, status);
    }

    @PatchMapping("/{interviewId}/feedback")
    public InterviewDto updateInterviewFeedback(@PathVariable String interviewId, @RequestBody String feedback) {
        return service.updateInterviewFeedback(interviewId, feedback);
    }

    @PostMapping("/{interviewId}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewQuestionDto createInterviewQuestion(@PathVariable String interviewId,
                                                        @RequestBody @Valid InterviewQuestionCreateDto dto) {
        return service.createInterviewQuestion(interviewId, dto);
    }

    @PatchMapping("/{interviewId}/questions/{questionId}")
    public InterviewQuestionDto updateInterviewQuestion(@PathVariable String interviewId,
                                                        @PathVariable String questionId,
                                                        @RequestBody @Valid InterviewQuestionUpdateDto dto) {
        return service.updateInterviewQuestion(interviewId, questionId, dto);
    }

    @DeleteMapping("/{interviewId}/questions/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInterviewQuestion(@PathVariable String interviewId, @PathVariable String questionId) {
        service.deleteInterviewQuestionById(interviewId, questionId);
    }
}
