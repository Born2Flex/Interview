package ua.edu.internship.interview.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.internship.interview.service.business.UserQuestionService;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionCreateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;
import java.util.List;

@Tag(name = "User Skills and Questions", description = "User skills and questions management endpoints")
@RestController
@RequestMapping("/users/{userId}/questions")
@RequiredArgsConstructor
public class UserQuestionController {
    private final UserQuestionService service;

    @GetMapping
    @Operation(summary = "Get user questions",
            description = "Retrieves a list of all questions for a specific user.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserQuestionDto.class))))
    @ApiResponse(responseCode = "404", description = "User not found")
    public List<UserQuestionDto> getUserQuestions(@PathVariable Long userId) {
        return service.getUserQuestions(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new user question",
            description = "Creates a new question for a specific user.")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserQuestionDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ResponseStatus(HttpStatus.CREATED)
    public UserQuestionDto createUserQuestion(@PathVariable Long userId, @RequestBody @Valid UserQuestionCreateDto dto) {
        return service.createUserQuestion(userId, dto);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "Update an existing user question",
            description = "Updates an existing user question.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserQuestionDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "User or question not found")
    public UserQuestionDto updateUserQuestion(@PathVariable Long userId, @PathVariable String questionId,
                                              @RequestBody UserQuestionUpdateDto dto) {
        return service.updateUserQuestion(userId, questionId, dto);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "Delete a user question",
            description = "Deletes a specific question for a user.")
    @ApiResponse(responseCode = "204", description = "Question deleted successfully")
    @ApiResponse(responseCode = "404", description = "User or question not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserQuestion(@PathVariable Long userId, @PathVariable String questionId) {
        service.deleteUserQuestion(userId, questionId);
    }

    @GetMapping("/skill/{skillId}")
    @Operation(summary = "Get user questions by skill",
            description = "Retrieves questions related to a specific skill for a user.")
    @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserQuestionDto.class))))
    @ApiResponse(responseCode = "404", description = "User or skill not found")
    public List<UserQuestionDto> getUserQuestionsBySkill(@PathVariable Long userId, @PathVariable String skillId) {
        return service.getUserQuestionsBySkillId(userId, skillId);
    }
}
