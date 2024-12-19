package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
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
import ua.edu.internship.interview.service.dto.user.question.UserQuestionCreateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserQuestionMapperTest {
    @Mock
    private BaseMapper baseMapper;
    @Mock
    private SkillMapper skillMapper;
    @InjectMocks
    private UserQuestionMapper underTest = new UserQuestionMapperImpl();

    @Test
    @DisplayName("Should map user question document to user question dto")
    void shouldMapUserQuestionDocumentToUserQuestionDto() {
        // given
        UserQuestionDocument questionDocument = createUserQuestionDocument();
        ObjectId skillId = questionDocument.getSkill().getId();
        ObjectId questionId = questionDocument.getId();
        when(baseMapper.map(skillId)).thenReturn(skillId.toString());
        when(baseMapper.map(questionId)).thenReturn(questionId.toString());

        // when
        UserQuestionDto result = underTest.toDto(questionDocument);

        // then
        assertNotNull(result);
        matchUserQuestionDocumentToDto(questionDocument, result);
    }

    @Test
    @DisplayName("Should map list of user question documents to list of user question dtos")
    void shouldMapListOfUserQuestionDocumentsToListOfUserQuestionDtos() {
        // given
        UserQuestionDocument questionDocument = createUserQuestionDocument();
        ObjectId skillId = questionDocument.getSkill().getId();
        ObjectId questionId = questionDocument.getId();
        when(baseMapper.map(skillId)).thenReturn(skillId.toString());
        when(baseMapper.map(questionId)).thenReturn(questionId.toString());
        List<UserQuestionDocument> documents = List.of(questionDocument);

        // when
        List<UserQuestionDto> result = underTest.toDto(documents);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        matchUserQuestionDocumentToDto(questionDocument, result.getFirst());
    }

    @Test
    @DisplayName("Should map user question create dto to user question document")
    void shouldMapUserQuestionCreateDtoToUserQuestionDocument() {
        // given
        long userId = 1L;
        UserQuestionCreateDto userQuestionCreateDto = createUserQuestionCreateDto();

        // when
        UserQuestionDocument result = underTest.toDocument(userId, userQuestionCreateDto);

        // then
        assertNotNull(result);
        matchUserQuestionCreateDtoToDocument(userId, userQuestionCreateDto, result);
    }

    @Test
    @DisplayName("Should update user question document with user question update dto")
    void updateDocument_shouldUpdateUserQuestionDocumentWithUserQuestionUpdateDto() {
        // given
        UserQuestionUpdateDto userQuestionUpdateDto = createUserQuestionUpdateDto();
        UserQuestionDocument userQuestionDocument = createUserQuestionDocument();

        // when
        underTest.updateDocument(userQuestionDocument, userQuestionUpdateDto);

        // then
        assertNotNull(userQuestionDocument);
        matchUserQuestionUpdateDtoToDocument(userQuestionUpdateDto, userQuestionDocument);
    }

    private UserQuestionDocument createUserQuestionDocument() {
        UserQuestionDocument questionDocument = new UserQuestionDocument();
        questionDocument.setId(new ObjectId("123456789123456789123456"));
        questionDocument.setUserId(1L);
        questionDocument.setText("Sample question text");
        questionDocument.setDifficulty(QuestionDifficulty.HARD);
        questionDocument.setType(QuestionType.SOFT_SKILLS);
        ObjectId skillId = new ObjectId("654321987654321987654321");
        SkillDocument skillDocument = new SkillDocument(skillId, "Programming", null);
        questionDocument.setSkill(skillDocument);
        return questionDocument;
    }

    private UserQuestionCreateDto createUserQuestionCreateDto() {
        UserQuestionCreateDto userQuestionCreateDto = new UserQuestionCreateDto();
        userQuestionCreateDto.setText("Complexity of merge sort algorithm?");
        userQuestionCreateDto.setSkillId("654321987654321987654321");
        userQuestionCreateDto.setDifficulty(QuestionDifficulty.MEDIUM);
        userQuestionCreateDto.setType(QuestionType.HARD_SKILLS);
        return userQuestionCreateDto;
    }

    private UserQuestionUpdateDto createUserQuestionUpdateDto() {
        UserQuestionUpdateDto userQuestionUpdateDto = new UserQuestionUpdateDto();
        userQuestionUpdateDto.setText("Updated question text");
        userQuestionUpdateDto.setDifficulty(QuestionDifficulty.EASY);
        userQuestionUpdateDto.setType(QuestionType.SOFT_SKILLS);
        return userQuestionUpdateDto;
    }

    private void matchUserQuestionDocumentToDto(UserQuestionDocument document, UserQuestionDto dto) {
        assertEquals(document.getId().toString(), dto.getId());
        assertEquals(document.getUserId(), dto.getUserId());
        assertEquals(document.getText(), dto.getText());
        assertEquals(document.getDifficulty(), dto.getDifficulty());
        assertEquals(document.getType(), dto.getType());
    }

    private void matchUserQuestionCreateDtoToDocument(Long userId, UserQuestionCreateDto createDto, UserQuestionDocument document) {
        assertEquals(userId, document.getUserId());
        assertEquals(createDto.getText(), document.getText());
        assertEquals(createDto.getDifficulty(), document.getDifficulty());
        assertEquals(createDto.getType(), document.getType());
    }

    private void matchUserQuestionUpdateDtoToDocument(UserQuestionUpdateDto updateDto, UserQuestionDocument document) {
        assertEquals(updateDto.getText(), document.getText());
        assertEquals(updateDto.getDifficulty(), document.getDifficulty());
        assertEquals(updateDto.getType(), document.getType());
    }
}
