package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.documents.InterviewDocument;
import ua.edu.internship.interview.data.documents.InterviewQuestionDocument;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;
import ua.edu.internship.interview.service.dto.interview.InterviewCreateDto;
import ua.edu.internship.interview.service.dto.interview.InterviewDto;
import ua.edu.internship.interview.service.dto.interview.InterviewUpdateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewDocument;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewDto;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDocument;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDto;
import static ua.edu.internship.interview.utils.TestUtils.createSkillDocument;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionDocument;

@ExtendWith(MockitoExtension.class)
class InterviewMapperTest {
    @Mock
    private BaseMapper baseMapper;
    @Mock
    private InterviewQuestionMapper interviewQuestionMapper;
    @InjectMocks
    private InterviewMapper underTest = new InterviewMapperImpl();

    private SkillDocument skillDocument;
    private UserQuestionDocument userQuestionDocument;
    private InterviewQuestionDocument interviewQuestionDocument;

    @BeforeEach
    public void setUp() {
        String skillId = "507f191e810c19729de860ea";
        skillDocument = createSkillDocument(skillId, "Programming languages");

        String questionId = "123456789123456789123456";
        Long userId = 1L;
        userQuestionDocument = createUserQuestionDocument(questionId, userId, skillDocument, "text",
                QuestionDifficulty.MEDIUM, QuestionType.HARD_SKILLS);

        String interviewQuestionId = "322456789123456789123456";
        interviewQuestionDocument = createInterviewQuestionDocument(interviewQuestionId, userQuestionDocument, 90);
    }

    @Test
    @DisplayName("Should map interview document to interview dto")
    void shouldMapDocumentToDto() {
        // given
        String interviewId = "748456789123456789123456";
        Long interviewerId = 1L;
        Long candidateId = 1L;
        InterviewDocument interviewDocument = createInterviewDocument(interviewId, interviewerId, candidateId,
                "Title", InterviewStatus.ACTIVE, List.of(interviewQuestionDocument));
        when(baseMapper.map(new ObjectId(interviewId))).thenReturn(interviewId);
        InterviewQuestionDto interviewQuestionDto = createInterviewQuestionDto(interviewQuestionDocument);
        when(interviewQuestionMapper.toDto(interviewQuestionDocument)).thenReturn(interviewQuestionDto);

        // when
        InterviewDto result = underTest.toDto(interviewDocument);

        // then
        assertNotNull(result);
        assertEquals(interviewId, result.getId());
        assertEquals(interviewerId, result.getInterviewerId());
        assertEquals(candidateId, result.getCandidateId());
        assertEquals("Title", result.getTitle());
        assertEquals(InterviewStatus.ACTIVE, result.getStatus());
        assertEquals(interviewDocument.getPlannedTime(), result.getPlannedTime());
        assertEquals(List.of(interviewQuestionDto), result.getQuestions());
        verify(baseMapper).map(new ObjectId(interviewId));
        verify(interviewQuestionMapper).toDto(interviewQuestionDocument);
    }

    @Test
    @DisplayName("Should map list of interview documents to list of interview dto")
    void shouldMapListOfDocumentsToListOfDtos() {
        // given
        String interviewId = "748456789123456789123456";
        Long interviewerId = 1L;
        Long candidateId = 2L;
        InterviewDocument interviewDocument = createInterviewDocument(interviewId, interviewerId, candidateId,
                "Title", InterviewStatus.ACTIVE, List.of(interviewQuestionDocument));
        when(baseMapper.map(new ObjectId(interviewId))).thenReturn(interviewId);
        InterviewQuestionDto interviewQuestionDto = createInterviewQuestionDto(interviewQuestionDocument);
        when(interviewQuestionMapper.toDto(interviewQuestionDocument)).thenReturn(interviewQuestionDto);

        // when
        List<InterviewDto> result = underTest.toDto(List.of(interviewDocument, interviewDocument));

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(createInterviewDto(interviewDocument), result.getFirst());
        verify(baseMapper, times(2)).map(new ObjectId(interviewId));
        verify(interviewQuestionMapper, times(2)).toDto(interviewQuestionDocument);
    }

    @Test
    @DisplayName("Should map interview creation dto to interview document")
    void shouldMapCreationDtoToDocument() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        LocalDateTime plannedTime = LocalDateTime.now();
        InterviewCreateDto interviewCreationDto =
                new InterviewCreateDto(interviewerId, candidateId, "Title", plannedTime);

        // when
        InterviewDocument result = underTest.toDocument(interviewCreationDto);

        // then
        assertNotNull(result);
        assertEquals(interviewerId, result.getInterviewerId());
        assertEquals(candidateId, result.getCandidateId());
        assertEquals("Title", result.getTitle());
        assertEquals(plannedTime, result.getPlannedTime());
    }

    @Test
    @DisplayName("Should update interview document data from interview update dto")
    void shouldUpdateDocument() {
        // given
        String interviewId = "748456789123456789123456";
        Long interviewerId = 1L;
        Long candidateId = 1L;
        InterviewDocument interviewDocument = createInterviewDocument(interviewId, interviewerId, candidateId,
                "Title", InterviewStatus.ACTIVE, List.of(interviewQuestionDocument));
        LocalDateTime plannedTime = LocalDateTime.now().plusDays(1);
        InterviewUpdateDto interviewUpdateDto = new InterviewUpdateDto("New title", plannedTime);

        // when
        InterviewDocument result = underTest.updateDocument(interviewDocument, interviewUpdateDto);

        // then
        assertNotNull(result);
        assertEquals("New title", result.getTitle());
        assertEquals(plannedTime, result.getPlannedTime());
        assertEquals(interviewerId, result.getInterviewerId());
        assertEquals(candidateId, result.getCandidateId());
    }
}
