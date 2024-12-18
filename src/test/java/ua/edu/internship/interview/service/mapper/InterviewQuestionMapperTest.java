package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.documents.InterviewQuestionDocument;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionUpdateDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDocument;
import static ua.edu.internship.interview.utils.TestUtils.createSkillDocument;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionDocument;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionDto;

@ExtendWith(MockitoExtension.class)
class InterviewQuestionMapperTest {
    @Mock
    private BaseMapper baseMapper;
    @Mock
    private UserQuestionMapper userQuestionMapper;
    @InjectMocks
    private InterviewQuestionMapper underTest = new InterviewQuestionMapperImpl();

    private SkillDocument skillDocument;
    private UserQuestionDocument userQuestionDocument;

    @BeforeEach
    public void setup() {
        String skillId = "507f191e810c19729de860ea";
        skillDocument = createSkillDocument(skillId, "Programming languages");

        String questionId = "123456789123456789123456";
        Long userId = 1L;
        userQuestionDocument = createUserQuestionDocument(questionId, userId, skillDocument, "text",
                QuestionDifficulty.MEDIUM, QuestionType.HARD_SKILLS);
    }

    @Test
    @DisplayName("Should map interview question document to dto")
    void shouldMapDocumentToDto() {
        // given
        String interviewQuestionId = "322456789123456789123456";
        InterviewQuestionDocument interviewQuestionDocument =
                createInterviewQuestionDocument(interviewQuestionId, userQuestionDocument, 85);
        UserQuestionDto userQuestionDto = createUserQuestionDto(userQuestionDocument);
        when(baseMapper.map(new ObjectId(interviewQuestionId))).thenReturn(interviewQuestionId);
        when(userQuestionMapper.toDto(userQuestionDocument)).thenReturn(userQuestionDto);

        // when
        InterviewQuestionDto result = underTest.toDto(interviewQuestionDocument);

        // then
        assertNotNull(result);
        assertEquals(interviewQuestionId, result.getId());
        assertEquals(85, result.getGrade());
        assertEquals(userQuestionDto, result.getQuestion());
        verify(baseMapper).map(new ObjectId(interviewQuestionId));
        verify(userQuestionMapper).toDto(userQuestionDocument);
    }

    @Test
    @DisplayName("Should update interview question with data from update dto")
    void shouldUpdateDocument() {
        // given
        String interviewQuestionId = "322456789123456789123456";
        InterviewQuestionDocument document =
                createInterviewQuestionDocument(interviewQuestionId, userQuestionDocument, 85);
        InterviewQuestionUpdateDto updateDto = new InterviewQuestionUpdateDto(99);

        // when
        InterviewQuestionDocument result = underTest.updateDocument(document, updateDto);

        // then
        assertNotNull(result);
        assertEquals(99, result.getGrade());
    }
}
