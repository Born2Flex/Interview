package ua.edu.internship.interview.service.business;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;
import ua.edu.internship.interview.data.repository.SkillRepository;
import ua.edu.internship.interview.data.repository.UserQuestionRepository;
import ua.edu.internship.interview.service.client.UserServiceClient;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionCreateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;
import ua.edu.internship.interview.service.mapper.UserQuestionMapper;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionDocument;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionUpdateDto;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserQuestionServiceTest {
    @Mock
    private UserQuestionRepository userQuestionRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private UserQuestionMapper userQuestionMapper;
    @Mock
    private UserServiceClient userClient;
    @InjectMocks
    private UserQuestionService underTest;

    private UserQuestionDocument userQuestionDocument;
    private UserQuestionDto userQuestionDto;
    private SkillDocument skillDocument;

    @BeforeEach
    void setUp() {
        skillDocument = SkillDocument.builder()
                .id(new ObjectId("123456789123456789123456"))
                .name("Java")
                .build();
        userQuestionDocument = UserQuestionDocument.builder()
                .userId(1L)
                .text("abc")
                .difficulty(QuestionDifficulty.MEDIUM)
                .type(QuestionType.HARD_SKILLS)
                .skill(skillDocument)
                .build();
        SkillDto skillDto = SkillDto.builder().id("123456789123456789123456").name("Java").build();
        userQuestionDto = UserQuestionDto.builder()
                .userId(1L)
                .text("abc")
                .difficulty(QuestionDifficulty.MEDIUM)
                .type(QuestionType.HARD_SKILLS)
                .skill(skillDto)
                .build();
    }

    @Test
    @DisplayName("Should return not empty list of user questions when questions exists for specified user")
    void shouldReturnUserQuestionsWhenSuchExists() {
        // given
        Long userId = 1L;
        when(userQuestionRepository.findAllByUserId(userId)).thenReturn(List.of(userQuestionDocument));
        when(userQuestionMapper.toDto(List.of(userQuestionDocument))).thenReturn(List.of(userQuestionDto));

        // when
        List<UserQuestionDto> result = underTest.getUserQuestions(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userQuestionRepository).findAllByUserId(userId);
    }

    @Test
    @DisplayName("Should return same user questions that is stored in DB for specified user")
    void shouldReturnSameQuestionsThatIsStoredInDB() {
        // given
        Long userId = 1L;
        when(userQuestionRepository.findAllByUserId(userId)).thenReturn(List.of(userQuestionDocument));
        when(userQuestionMapper.toDto(List.of(userQuestionDocument))).thenReturn(List.of(userQuestionDto));

        // when
        List<UserQuestionDto> result = underTest.getUserQuestions(userId);

        // then
        assertNotNull(result);
        matchQuestionFields(userQuestionDto, result.getFirst());
    }

    @Test
    @DisplayName("Should create user question when specified user and skill exists")
    void shouldCreateUserQuestion() {
        // given
        Long userId = 1L;
        String skillId = "123456789123456789123456";
        ObjectId objectId = new ObjectId(skillId);
        UserQuestionCreateDto createDto = UserQuestionCreateDto.builder().text("abc").skillId(skillId)
                .difficulty(QuestionDifficulty.MEDIUM).type(QuestionType.HARD_SKILLS).build();
        when(userClient.existsById(userId)).thenReturn(true);
        when(skillRepository.findById(objectId)).thenReturn(Optional.of(skillDocument));
        when(userQuestionMapper.toDocument(userId, createDto)).thenReturn(userQuestionDocument);
        when(userQuestionRepository.save(userQuestionDocument)).thenReturn(userQuestionDocument);
        when(userQuestionMapper.toDto(userQuestionDocument)).thenReturn(userQuestionDto);

        // when
        UserQuestionDto result = underTest.createUserQuestion(userId, createDto);

        // then
        assertNotNull(result);
        verify(userQuestionRepository).save(userQuestionDocument);
        verify(skillRepository).findById(objectId);
        verify(userQuestionMapper).toDocument(userId, createDto);
        verify(userQuestionRepository).save(userQuestionDocument);
        verify(userQuestionMapper).toDto(userQuestionDocument);
    }

    @Test
    @DisplayName("Should create user question with the same data as in creation dto")
    void shouldCreateUserQuestionWithSameDataAsInCreationDto() {
        // given
        Long userId = 1L;
        String skillId = "123456789123456789123456";
        ObjectId objectId = new ObjectId(skillId);
        UserQuestionCreateDto createDto = UserQuestionCreateDto.builder().text("abc").skillId(skillId)
                .difficulty(QuestionDifficulty.MEDIUM).type(QuestionType.HARD_SKILLS).build();
        when(userClient.existsById(userId)).thenReturn(true);
        when(skillRepository.findById(objectId)).thenReturn(Optional.of(skillDocument));
        when(userQuestionMapper.toDocument(userId, createDto)).thenReturn(userQuestionDocument);
        when(userQuestionRepository.save(userQuestionDocument)).thenReturn(userQuestionDocument);
        when(userQuestionMapper.toDto(userQuestionDocument)).thenReturn(userQuestionDto);

        // when
        UserQuestionDto result = underTest.createUserQuestion(userId, createDto);

        // then
        assertNotNull(result);
        matchQuestionFields(userQuestionDto, result);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when user for which the question is created not found")
    void shouldThrowNoSuchEntityExceptionWhenUserNotFound() {
        // given
        Long userId = 1L;
        UserQuestionCreateDto questionDto = new UserQuestionCreateDto();
        when(userClient.existsById(userId)).thenReturn(false);

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createUserQuestion(userId, questionDto));
        verify(userClient).existsById(userId);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when question skill not found when creating new question for user")
    void shouldThrowNoSuchEntityExceptionWhenSkillNotFound() {
        // given
        Long userId = 1L;
        String skillId = "123456789123456789123456";
        ObjectId objectId = new ObjectId(skillId);
        UserQuestionCreateDto questionDto = new UserQuestionCreateDto();
        questionDto.setSkillId(skillId);
        when(userClient.existsById(userId)).thenReturn(true);
        when(skillRepository.findById(objectId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createUserQuestion(userId, questionDto));
        verify(skillRepository).findById(objectId);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when question which is updated not found")
    void shouldThrowNoSuchEntityExceptionWhenUpdatedQuestionNotFound() {
        // given
        Long userId = 1L;
        String questionId = "123456789123456789123456";
        String skillId = "654321987654321987654321";
        ObjectId questionObjectId = new ObjectId(questionId);
        UserQuestionUpdateDto updateDto = createUserQuestionUpdateDto("Updated text", skillId,
                QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        when(userQuestionRepository.findByIdAndUserId(questionObjectId, userId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateUserQuestion(userId, questionId, updateDto));
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when skill in update dto not found")
    void shouldThrowNoSuchEntityExceptionWhenUpdatedSkillNotFound() {
        // given
        Long userId = 1L;
        String questionId = "123456789123456789123456";
        String skillId = "654321987654321987654321";
        ObjectId questionObjectId = new ObjectId(questionId);
        ObjectId skillObjectId = new ObjectId(skillId);
        UserQuestionUpdateDto updateDto = createUserQuestionUpdateDto("Updated text", skillId,
                QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        when(userQuestionRepository.findByIdAndUserId(questionObjectId, userId)).thenReturn(Optional.of(userQuestionDocument));
        when(skillRepository.findById(skillObjectId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateUserQuestion(userId, questionId, updateDto));
    }

    @Test
    @DisplayName("Should update user question when user and skill exists")
    void shouldUpdateAndReturnUserQuestionDtoWhenQuestionExists() {
        // given
        Long userId = 1L;
        String questionId = "123456789123456789123456";
        String skillId = "654321987654321987654321";
        ObjectId questionObjectId = new ObjectId(questionId);
        ObjectId skillObjectId = new ObjectId(skillId);
        SkillDocument updatedSkillDocument = SkillDocument.builder().id(skillObjectId).name("Haskell").build();
        UserQuestionUpdateDto updateDto = createUserQuestionUpdateDto(
                "Updated text", skillId, QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        UserQuestionDocument updatedDocument = createUserQuestionDocument(
                questionId, userId, updatedSkillDocument, "Updated text", QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        when(userQuestionRepository.findByIdAndUserId(questionObjectId, userId)).thenReturn(Optional.of(userQuestionDocument));
        when(skillRepository.findById(skillObjectId)).thenReturn(Optional.of(updatedSkillDocument));
        when(userQuestionMapper.updateDocument(userQuestionDocument, updateDto)).thenReturn(updatedDocument);
        when(userQuestionRepository.save(updatedDocument)).thenReturn(updatedDocument);
        when(userQuestionMapper.toDto(updatedDocument)).thenReturn(userQuestionDto);

        // when
        UserQuestionDto result = underTest.updateUserQuestion(userId, questionId, updateDto);

        // then
        assertNotNull(result);
        verify(userQuestionRepository).findByIdAndUserId(questionObjectId, userId);
        verify(skillRepository).findById(skillObjectId);
        verify(userQuestionMapper).updateDocument(userQuestionDocument, updateDto);
        verify(userQuestionRepository).save(updatedDocument);
        verify(userQuestionMapper).toDto(updatedDocument);
    }

    @Test
    @DisplayName("Should update user question with same data as in update dto")
    void shouldUpdateQuestionWithSameDataAsInUpdateDto() {
        // given
        Long userId = 1L;
        String questionId = "123456789123456789123456";
        String skillId = "654321987654321987654321";
        ObjectId questionObjectId = new ObjectId(questionId);
        ObjectId skillObjectId = new ObjectId(skillId);
        SkillDocument updatedSkillDocument = SkillDocument.builder().id(skillObjectId).name("Haskell").build();
        UserQuestionUpdateDto updateDto = createUserQuestionUpdateDto(
                "Updated text", skillId, QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        UserQuestionDocument updatedDocument = createUserQuestionDocument(
                questionId, userId, updatedSkillDocument, "Updated text", QuestionDifficulty.EASY, QuestionType.SOFT_SKILLS);
        when(userQuestionRepository.findByIdAndUserId(questionObjectId, userId)).thenReturn(Optional.of(userQuestionDocument));
        when(skillRepository.findById(skillObjectId)).thenReturn(Optional.of(updatedSkillDocument));
        when(userQuestionMapper.updateDocument(userQuestionDocument, updateDto)).thenReturn(updatedDocument);
        when(userQuestionRepository.save(updatedDocument)).thenReturn(updatedDocument);
        when(userQuestionMapper.toDto(updatedDocument)).thenReturn(userQuestionDto);

        // when
        UserQuestionDto result = underTest.updateUserQuestion(userId, questionId, updateDto);

        // then
        assertNotNull(result);
        matchQuestionFields(userQuestionDto, result);
    }

    @Test
    @DisplayName("Should delete user question for specified user")
    void shouldDeleteUserQuestion() {
        // given
        Long userId = 1L;
        String questionId = "123456789123456789123456";
        doNothing().when(userQuestionRepository).deleteByUserIdAndSkillId(userId, new ObjectId(questionId));

        // when
        underTest.deleteUserQuestion(userId, questionId);

        // then
        verify(userQuestionRepository).deleteByUserIdAndSkillId(userId, new ObjectId(questionId));
    }

    @Test
    @DisplayName("Should return not empty list of user questions by skill when questions exists for specified user")
    void shouldReturnNotEmptyListOfUserQuestionBySkillForUser() {
        // given
        Long userId = 1L;
        String skillId = "123456789123456789123456";
        List<UserQuestionDocument> questions = List.of(userQuestionDocument);
        when(userQuestionRepository.findAllByUserIdAndSkillId(userId, new ObjectId(skillId))).thenReturn(questions);
        when(userQuestionMapper.toDto(questions)).thenReturn(List.of(userQuestionDto));

        // when
        List<UserQuestionDto> result = underTest.getUserQuestionsBySkillId(userId, skillId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userQuestionRepository).findAllByUserIdAndSkillId(userId, new ObjectId(skillId));
    }

    @Test
    @DisplayName("Should return list of user questions for specified skill, that is stored in DB for user")
    void shouldReturnListOfUserQuestionBySkillThatIsStoredInDB() {
        // given
        Long userId = 1L;
        String skillId = "123456789123456789123456";
        List<UserQuestionDocument> questions = List.of(userQuestionDocument);
        when(userQuestionRepository.findAllByUserIdAndSkillId(userId, new ObjectId(skillId))).thenReturn(questions);
        when(userQuestionMapper.toDto(questions)).thenReturn(List.of(userQuestionDto));

        // when
        List<UserQuestionDto> result = underTest.getUserQuestionsBySkillId(userId, skillId);

        // then
        assertNotNull(result);
        matchQuestionFields(userQuestionDto, result.getFirst());
    }

    private void matchQuestionFields(UserQuestionDto expected, UserQuestionDto actual) {
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getDifficulty(), actual.getDifficulty());
        assertEquals(expected.getType(), actual.getType());
        matchSkillFields(actual.getSkill(), expected.getSkill());
    }

    private void matchSkillFields(SkillDto expected, SkillDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
