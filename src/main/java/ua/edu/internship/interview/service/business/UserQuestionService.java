package ua.edu.internship.interview.service.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.data.repository.SkillRepository;
import ua.edu.internship.interview.data.repository.UserQuestionRepository;
import ua.edu.internship.interview.service.client.UserServiceClient;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionCreateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;
import ua.edu.internship.interview.service.mapper.UserQuestionMapper;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserQuestionService {
    private final UserQuestionRepository userQuestionRepository;
    private final SkillRepository skillRepository;
    private final UserQuestionMapper mapper;
    private final UserServiceClient userClient;

    public List<UserQuestionDto> getUserQuestions(Long userId) {
        List<UserQuestionDocument> questions = userQuestionRepository.findAllByUserId(userId);
        log.info("Retrieved {} questions for user with id: {}", questions.size(), userId);
        return mapper.toDto(questions);
    }

    public UserQuestionDto createUserQuestion(Long userId, UserQuestionCreateDto dto) {
        log.info("Attempting to create new question for user with id: {}", userId);
        validateUserExistsById(userId);
        SkillDocument skill = getSkillByIdOrElseThrow(dto.getSkillId());
        UserQuestionDocument questionDocument = mapper.toDocument(userId, dto);
        questionDocument.setSkill(skill);
        UserQuestionDocument savedQuestion = userQuestionRepository.save(questionDocument);
        log.info("Created new question with id: {} for user with id: {}", savedQuestion.getId(), userId);
        return mapper.toDto(savedQuestion);
    }

    public UserQuestionDto updateUserQuestion(Long userId, String questionId, UserQuestionUpdateDto updateDto) {
        log.info("Attempting to update question with id: {} for user with id: {}", questionId, userId);
        UserQuestionDocument questionDocument = getQuestionByUserIdOrElseThrow(userId, questionId);
        SkillDocument skillDocument = getSkillByIdOrElseThrow(updateDto.getSkillId());
        UserQuestionDocument updatedQuestionDocument = mapper.updateDocument(questionDocument, updateDto);
        updatedQuestionDocument.setSkill(skillDocument);
        UserQuestionDocument updatedQuestion = userQuestionRepository.save(updatedQuestionDocument);
        log.info("Updated question with id: {} for user with id: {}", updatedQuestion.getId(), userId);
        return mapper.toDto(updatedQuestion);
    }

    public void deleteUserQuestion(Long userId, String questionId) {
        log.info("Attempting to delete question with id: {}, for user with id: {}", questionId, userId);
        userQuestionRepository.deleteByUserIdAndSkillId(userId, new ObjectId(questionId));
        log.info("Question with id: {} deleted successfully, for user with id: {}", questionId, userId);
    }

    public List<UserQuestionDto> getUserQuestionsBySkillId(Long userId, String skillId) {
        List<UserQuestionDocument> questions = userQuestionRepository
                .findAllByUserIdAndSkillId(userId, new ObjectId(skillId));
        log.info("Retrieved {} questions for skill with id: {} for user with id: {}", questions.size(), skillId, userId);
        return mapper.toDto(questions);
    }

    private SkillDocument getSkillByIdOrElseThrow(String skillId) {
        return skillRepository.findById(new ObjectId(skillId))
                .orElseThrow(() -> new NoSuchEntityException("Skill not found by id: " + skillId));
    }

    private UserQuestionDocument getQuestionByUserIdOrElseThrow(Long userId, String questionId) {
        return userQuestionRepository.findByIdAndUserId(new ObjectId(questionId), userId)
                .orElseThrow(() -> new NoSuchEntityException(
                        "Question not found by id: " + questionId + " for user with id: " + userId
                ));
    }

    private void validateUserExistsById(Long userId) {
        if (!userClient.existsById(userId)) {
            throw new NoSuchEntityException("User not found by id: " + userId);
        }
    }
}
