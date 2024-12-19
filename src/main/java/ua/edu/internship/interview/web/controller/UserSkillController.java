package ua.edu.internship.interview.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.internship.interview.service.business.UserSkillsService;
import ua.edu.internship.interview.service.dto.user.skill.UserSkillsDto;
import java.util.List;

@Tag(name = "User Skills and Questions", description = "User skills and questions management endpoints")
@RestController
@RequestMapping("/users/{userId}/skills")
@RequiredArgsConstructor
public class UserSkillController {
    private final UserSkillsService service;

    @GetMapping
    @Operation(summary = "Get skills by user ID",
            description = "Retrieves a list of skills associated with a specific user.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserSkillsDto.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public UserSkillsDto getSkillsByUserId(@PathVariable Long userId) {
        return service.getUserSkills(userId);
    }

    @PostMapping
    @Operation(summary = "Create user skills",
            description = "Creates a set of skills for a user based on skill IDs.")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = UserSkillsDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ResponseStatus(HttpStatus.CREATED)
    public UserSkillsDto createUserSkills(@PathVariable Long userId, @RequestBody List<String> skillIds) {
        return service.createUserSkills(userId, skillIds);
    }

    @PutMapping
    @Operation(summary = "Update user skills",
            description = "Updates the skills for a user based on skill IDs.")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = UserSkillsDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public UserSkillsDto updateUserSkills(@PathVariable Long userId, @RequestBody List<String> skillIds) {
        return service.updateUserSkills(userId, skillIds);
    }
}
