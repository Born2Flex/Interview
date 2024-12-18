package ua.edu.internship.interview.service.business;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import ua.edu.internship.interview.data.repository.InterviewRepository;
import ua.edu.internship.interview.data.repository.UserQuestionRepository;
import ua.edu.internship.interview.service.client.UserServiceClient;
import ua.edu.internship.interview.service.dto.interview.InterviewCreateDto;
import ua.edu.internship.interview.service.dto.interview.InterviewDto;
import ua.edu.internship.interview.service.dto.interview.InterviewUpdateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionCreateDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionUpdateDto;
import ua.edu.internship.interview.service.mapper.InterviewMapper;
import ua.edu.internship.interview.service.mapper.InterviewQuestionMapper;
import ua.edu.internship.interview.service.utils.exceptions.InterviewCollisionException;
import ua.edu.internship.interview.service.utils.exceptions.InvalidStatusTransitionException;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.edu.internship.interview.service.business.InterviewService.TIME_WINDOW_OFFSET_IN_HOURS;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewDocument;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewDto;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDocument;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDocumentWithoutId;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewQuestionDto;
import static ua.edu.internship.interview.utils.TestUtils.createInterviewUpdateDto;
import static ua.edu.internship.interview.utils.TestUtils.createSkillDocument;
import static ua.edu.internship.interview.utils.TestUtils.createUserQuestionDocument;

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {
    @Mock
    private InterviewRepository interviewRepository;
    @Mock
    private UserQuestionRepository userQuestionRepository;
    @Mock
    private InterviewMapper interviewMapper;
    @Mock
    private InterviewQuestionMapper questionMapper;
    @Mock
    private UserServiceClient userClient;
    @InjectMocks
    private InterviewService underTest;

    private SkillDocument skillDocument;
    private UserQuestionDocument userQuestionDocument;
    private InterviewQuestionDocument interviewQuestionDocument;
    private InterviewDocument interviewDocument;
    private InterviewDocument newInterviewDocument;

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

        String interviewId = "748456789123456789123456";
        Long interviewerId = 1L;
        Long candidateId = 2L;
        interviewDocument = createInterviewDocument(interviewId, interviewerId, candidateId, "Title",
                InterviewStatus.ACTIVE, List.of(interviewQuestionDocument));
        newInterviewDocument = createInterviewDocument(interviewId, interviewerId, candidateId, "Title",
                InterviewStatus.PLANNED, List.of(interviewQuestionDocument));
    }

    @Test
    @DisplayName("Should return all existing interviews from DB")
    void shouldReturnAllInterviews() {
        // given
        InterviewDto interviewDto = createInterviewDto(interviewDocument);
        when(interviewRepository.findAll()).thenReturn(List.of(interviewDocument));
        when(interviewMapper.toDto(List.of(interviewDocument))).thenReturn(List.of(interviewDto));

        // when
        List<InterviewDto> result = underTest.getAllInterviews();

        // then
        assertNotNull(result);
        verify(interviewRepository).findAll();
        verify(interviewMapper).toDto(List.of(interviewDocument));
    }

    @Test
    @DisplayName("Should return same interviews that is stored in DB")
    void shouldReturnAllInterviewsAsInDb() {
        // given
        InterviewDto interviewDto = createInterviewDto(interviewDocument);
        when(interviewRepository.findAll()).thenReturn(List.of(interviewDocument));
        when(interviewMapper.toDto(List.of(interviewDocument))).thenReturn(List.of(interviewDto));

        // when
        List<InterviewDto> result = underTest.getAllInterviews();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        matchInterviewFields(createInterviewDto(interviewDocument), result.getFirst());
        verify(interviewRepository).findAll();
        verify(interviewMapper).toDto(List.of(interviewDocument));
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when interview not found by id")
    void shouldThrowNoSuchEntityExceptionWhenInterviewNotFoundById() {
        // given
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.getInterviewById(interviewId));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should return interview by id when exists")
    void shouldReturnInterviewByIdWhenExists() {
        // given
        String interviewId = "507f191e810c19729de860ea";
        InterviewDto interviewDto = createInterviewDto(interviewDocument);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(interviewMapper.toDto(interviewDocument)).thenReturn(interviewDto);

        // when
        InterviewDto result = underTest.getInterviewById(interviewId);

        // then
        assertNotNull(result);
        matchInterviewFields(createInterviewDto(interviewDocument), result);
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).toDto(interviewDocument);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when interviewer does not exists")
    void shouldThrowNoSuchEntityExceptionWhenInterviewerDoesNotExist() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        LocalDateTime plannedTime = LocalDateTime.now();
        InterviewCreateDto interviewCreationDto =
                new InterviewCreateDto(interviewerId, candidateId, "Title", plannedTime);
        when(interviewMapper.toDocument(interviewCreationDto)).thenReturn(interviewDocument);
        when(userClient.existsById(interviewerId)).thenReturn(false);

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createInterview(interviewCreationDto));
        verify(interviewMapper).toDocument(interviewCreationDto);
        verify(userClient).existsById(interviewerId);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when candidate does not exists")
    void shouldThrowNoSuchEntityExceptionWhenCandidateDoesNotExist() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        LocalDateTime plannedTime = LocalDateTime.now();
        InterviewCreateDto interviewCreationDto =
                new InterviewCreateDto(interviewerId, candidateId, "Title", plannedTime);
        when(interviewMapper.toDocument(interviewCreationDto)).thenReturn(interviewDocument);
        when(userClient.existsById(interviewerId)).thenReturn(true);
        when(userClient.existsById(candidateId)).thenReturn(false);

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createInterview(interviewCreationDto));
        verify(interviewMapper).toDocument(interviewCreationDto);
        verify(userClient).existsById(interviewerId);
        verify(userClient).existsById(candidateId);
    }

    @Test
    @DisplayName("Should throw InterviewCollisionException when interview collision occurs")
    void shouldThrowInterviewCollisionExceptionWhenInterviewCollisionOccurs() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        LocalDateTime from = interviewDocument.getPlannedTime().minusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        LocalDateTime to = interviewDocument.getPlannedTime().plusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        InterviewCreateDto interviewCreationDto =
                new InterviewCreateDto(interviewerId, candidateId, "Title", LocalDateTime.now());
        when(interviewMapper.toDocument(interviewCreationDto)).thenReturn(interviewDocument);
        when(userClient.existsById(interviewerId)).thenReturn(true);
        when(userClient.existsById(candidateId)).thenReturn(true);
        when(interviewRepository.existsInterviewsInTimeWindow(interviewerId, candidateId, from, to)).thenReturn(true);

        // when
        // then
        assertThrows(InterviewCollisionException.class, () -> underTest.createInterview(interviewCreationDto));
        verify(interviewMapper).toDocument(interviewCreationDto);
        verify(userClient).existsById(interviewerId);
        verify(userClient).existsById(candidateId);
        verify(interviewRepository).existsInterviewsInTimeWindow(interviewerId, candidateId, from, to);
    }

    @Test
    @DisplayName("Should create new interview")
    void shouldCreateNewInterview() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        LocalDateTime from = interviewDocument.getPlannedTime().minusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        LocalDateTime to = interviewDocument.getPlannedTime().plusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        InterviewCreateDto interviewCreationDto =
                new InterviewCreateDto(interviewerId, candidateId, "Title", LocalDateTime.now());
        when(interviewMapper.toDocument(interviewCreationDto)).thenReturn(interviewDocument);
        when(userClient.existsById(interviewerId)).thenReturn(true);
        when(userClient.existsById(candidateId)).thenReturn(true);
        when(interviewRepository.existsInterviewsInTimeWindow(interviewerId, candidateId, from, to)).thenReturn(false);
        when(interviewRepository.save(newInterviewDocument)).thenReturn(newInterviewDocument);
        when(interviewMapper.toDto(newInterviewDocument)).thenReturn(createInterviewDto(newInterviewDocument));

        // when
        InterviewDto result = underTest.createInterview(interviewCreationDto);

        // then
        assertNotNull(result);
        matchInterviewFields(createInterviewDto(newInterviewDocument), result);
        verify(interviewMapper).toDocument(interviewCreationDto);
        verify(userClient).existsById(interviewerId);
        verify(userClient).existsById(candidateId);
        verify(interviewRepository).existsInterviewsInTimeWindow(interviewerId, candidateId, from, to);
        verify(interviewRepository).save(newInterviewDocument);
        verify(interviewMapper).toDto(newInterviewDocument);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when updated interview does not exist")
    void shouldThrowNoSuchEntityExceptionWhenUpdatedInterviewDoesNotExist() {
        // given
        String interviewId = "748456789123456789123456";
        InterviewUpdateDto interviewUpdateDto = createInterviewUpdateDto("New title", LocalDateTime.now().plusDays(1));
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateInterview(interviewId, interviewUpdateDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should throw InterviewCollisionException when updating interview and collision occurs")
    void shouldThrowInterviewCollisionExceptionWhenUpdatingInterviewAndCollisionOccurs() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        String interviewId = "748456789123456789123456";
        InterviewUpdateDto interviewUpdateDto = createInterviewUpdateDto("New title", LocalDateTime.now().plusDays(1));
        LocalDateTime from = interviewUpdateDto.getPlannedTime().minusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        LocalDateTime to = interviewUpdateDto.getPlannedTime().plusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        newInterviewDocument.setPlannedTime(interviewUpdateDto.getPlannedTime());
        newInterviewDocument.setTitle(interviewUpdateDto.getTitle());
        when(interviewMapper.updateDocument(interviewDocument, interviewUpdateDto)).thenReturn(newInterviewDocument);
        when(interviewRepository.existsInterviewsInTimeWindow(interviewerId, candidateId, from, to)).thenReturn(true);

        // when
        // then
        assertThrows(InterviewCollisionException.class, () -> underTest.updateInterview(interviewId, interviewUpdateDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).updateDocument(interviewDocument, interviewUpdateDto);
        verify(interviewRepository).existsInterviewsInTimeWindow(interviewerId, candidateId, from, to);
    }

    @Test
    @DisplayName("Should update planned interview")
    void shouldUpdatePlannedInterview() {
        // given
        Long interviewerId = 1L;
        Long candidateId = 2L;
        String interviewId = "748456789123456789123456";
        InterviewUpdateDto interviewUpdateDto = createInterviewUpdateDto("New title", LocalDateTime.now().plusDays(1));
        LocalDateTime from = interviewUpdateDto.getPlannedTime().minusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        LocalDateTime to = interviewUpdateDto.getPlannedTime().plusHours(TIME_WINDOW_OFFSET_IN_HOURS);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        newInterviewDocument.setPlannedTime(interviewUpdateDto.getPlannedTime());
        newInterviewDocument.setTitle(interviewUpdateDto.getTitle());
        when(interviewMapper.updateDocument(interviewDocument, interviewUpdateDto)).thenReturn(newInterviewDocument);
        when(interviewRepository.existsInterviewsInTimeWindow(interviewerId, candidateId, from, to)).thenReturn(false);
        when(interviewRepository.save(newInterviewDocument)).thenReturn(newInterviewDocument);
        when(interviewMapper.toDto(newInterviewDocument)).thenReturn(createInterviewDto(newInterviewDocument));

        // when
        InterviewDto result = underTest.updateInterview(interviewId, interviewUpdateDto);

        // then
        assertNotNull(result);
        matchInterviewFields(createInterviewDto(newInterviewDocument), result);
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).updateDocument(interviewDocument, interviewUpdateDto);
        verify(interviewRepository).existsInterviewsInTimeWindow(interviewerId, candidateId, from, to);
        verify(interviewRepository).save(newInterviewDocument);
        verify(interviewMapper).toDto(newInterviewDocument);
    }

    @Test
    @DisplayName("Should delete interview")
    void shouldDeleteUserQuestion() {
        // given
        String interviewId = "748456789123456789123456";
        doNothing().when(interviewRepository).deleteById(new ObjectId(interviewId));

        // when
        underTest.deleteInterviewById(interviewId);

        // then
        verify(interviewRepository).deleteById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to update status of interview that not exists")
    void shouldThrowNoSuchEntityExceptionWhenTryingToUpdateStatusOfInterviewThatNotExists() {
        // given
        InterviewStatus status = InterviewStatus.ACTIVE;
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateInterviewStatus(interviewId, status));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should throw InvalidStateTransitionException when trying to set invalid status of interview")
    void shouldThrowInvalidStateTransitionExceptionWhenTryingToSetInvalidStatusOfInterview() {
        // given
        InterviewStatus status = InterviewStatus.PLANNED;
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));

        // when
        // then
        assertThrows(InvalidStatusTransitionException.class, () -> underTest.updateInterviewStatus(interviewId, status));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should update interview status and interview start time when status ACTIVE")
    void shouldUpdateStatusAndStartTimeOfInterviewWhenActive() {
        // given
        InterviewStatus status = InterviewStatus.ACTIVE;
        String interviewId = "748456789123456789123456";
        interviewDocument.setStatus(InterviewStatus.PLANNED);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        ArgumentCaptor<InterviewDocument> captor = ArgumentCaptor.forClass(InterviewDocument.class);
        when(interviewRepository.save(captor.capture())).thenReturn(interviewDocument);
        when(interviewMapper.toDto(interviewDocument)).thenReturn(createInterviewDto(interviewDocument));

        // when
        InterviewDto result = underTest.updateInterviewStatus(interviewId, status);

        // then
        assertNotNull(result);
        InterviewDocument capturedDocument = captor.getValue();
        assertNotNull(capturedDocument);
        assertNotNull(capturedDocument.getStartTime());
        assertEquals(InterviewStatus.ACTIVE, capturedDocument.getStatus());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).toDto(interviewDocument);
    }

    @Test
    @DisplayName("Should update interview status and interview end time when status COMPLETED")
    void shouldUpdateStatusAndEndTimeOfInterviewWhenCompleted() {
        // given
        InterviewStatus status = InterviewStatus.COMPLETED;
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        ArgumentCaptor<InterviewDocument> captor = ArgumentCaptor.forClass(InterviewDocument.class);
        when(interviewRepository.save(captor.capture())).thenReturn(interviewDocument);
        when(interviewMapper.toDto(interviewDocument)).thenReturn(createInterviewDto(interviewDocument));

        // when
        InterviewDto result = underTest.updateInterviewStatus(interviewId, status);

        // then
        assertNotNull(result);
        InterviewDocument capturedDocument = captor.getValue();
        assertNotNull(capturedDocument);
        assertNotNull(capturedDocument.getEndTime());
        assertEquals(InterviewStatus.COMPLETED, capturedDocument.getStatus());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).toDto(interviewDocument);
    }

    @Test
    @DisplayName("Should cancel interview when updating status on CANCELED")
    void shouldCancelInterviewWhenUpdatingStatusOnCanceled() {
        // given
        InterviewStatus status = InterviewStatus.CANCELLED;
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        ArgumentCaptor<InterviewDocument> captor = ArgumentCaptor.forClass(InterviewDocument.class);
        when(interviewRepository.save(captor.capture())).thenReturn(interviewDocument);
        when(interviewMapper.toDto(interviewDocument)).thenReturn(createInterviewDto(interviewDocument));

        // when
        InterviewDto result = underTest.updateInterviewStatus(interviewId, status);

        // then
        assertNotNull(result);
        InterviewDocument capturedDocument = captor.getValue();
        assertNotNull(capturedDocument);
        assertEquals(InterviewStatus.CANCELLED, capturedDocument.getStatus());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewMapper).toDto(interviewDocument);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to add feedback to interview that not exists")
    void shouldThrowNoSuchEntityExceptionWhenAddFeedbackToInterviewThatNotExists() {
        // given
        String interviewId = "748456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateInterviewFeedback(interviewId, "feedback"));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should update feedback for interview")
    void shouldUpdateFeedbackForInterview() {
        // given
        String interviewId = "748456789123456789123456";
        interviewDocument.setStatus(InterviewStatus.PLANNED);
        newInterviewDocument.setFeedback("feedback");
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(interviewRepository.save(newInterviewDocument)).thenReturn(newInterviewDocument);
        when(interviewMapper.toDto(newInterviewDocument)).thenReturn(createInterviewDto(newInterviewDocument));

        // when
        InterviewDto result = underTest.updateInterviewFeedback(interviewId, "feedback");
        // then
        assertNotNull(result);
        assertEquals("feedback", result.getFeedback());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewRepository).save(newInterviewDocument);
        verify(interviewMapper).toDto(newInterviewDocument);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to add question to interview that not exists")
    void shouldThrowNoSuchEntityExceptionWhenAddQuestionToInterviewThatNotExists() {
        // given
        String interviewId = "748456789123456789123456";
        String userQuestionId = "998456789123456789123456";
        InterviewQuestionCreateDto createDto = new InterviewQuestionCreateDto(userQuestionId);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createInterviewQuestion(interviewId, createDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to add user question that not exist to interview")
    void shouldThrowNoSuchEntityExceptionWhenAddUserQuestionToInterviewThatNotExists() {
        // given
        String interviewId = "748456789123456789123456";
        String userQuestionId = "998456789123456789123456";
        InterviewQuestionCreateDto createDto = new InterviewQuestionCreateDto(userQuestionId);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(userQuestionRepository.findById(new ObjectId(userQuestionId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.createInterviewQuestion(interviewId, createDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should create interview question")
    void shouldCreateInterviewQuestion() {
        // given
        String interviewId = "748456789123456789123456";
        String userQuestionId = "998456789123456789123456";
        InterviewQuestionCreateDto createDto = new InterviewQuestionCreateDto(userQuestionId);
        interviewDocument.setQuestions(new ArrayList<>());
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(userQuestionRepository.findById(new ObjectId(userQuestionId))).thenReturn(Optional.of(userQuestionDocument));
        InterviewQuestionDocument createdQuestion = createInterviewQuestionDocumentWithoutId(userQuestionDocument, null);
        when(interviewRepository.save(interviewDocument)).thenReturn(interviewDocument);
        when(questionMapper.toDto(createdQuestion)).thenReturn(createInterviewQuestionDto(createdQuestion));

        // when
        InterviewQuestionDto result = underTest.createInterviewQuestion(interviewId, createDto);

        // then
        assertNotNull(result);
        assertEquals(1, interviewDocument.getQuestions().size());
        assertEquals(createdQuestion, interviewDocument.getQuestions().getFirst());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(userQuestionRepository).findById(new ObjectId(userQuestionId));
        verify(interviewRepository).save(interviewDocument);
        verify(questionMapper).toDto(createdQuestion);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to update question and interview not exists")
    void shouldThrowNoSuchEntityExceptionWhenUpdateQuestionInInterviewThatNotExists() {
        // given
        String interviewId = "748456789123456789123456";
        String interviewQuestionId = "998456789123456789123456";
        InterviewQuestionUpdateDto updateDto = new InterviewQuestionUpdateDto(60);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateInterviewQuestion(interviewId, interviewQuestionId, updateDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to update question that not exists in interview")
    void shouldThrowNoSuchEntityExceptionWhenUpdateQuestionThatNotExistsInInterview() {
        // given
        String interviewId = "748456789123456789123456";
        String interviewQuestionId = "998456789123456789123456";
        InterviewQuestionUpdateDto updateDto = new InterviewQuestionUpdateDto(60);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.updateInterviewQuestion(interviewId, interviewQuestionId, updateDto));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should update interview question")
    void shouldUpdateInterviewQuestion() {
        // given
        String interviewId = "748456789123456789123456";
        String interviewQuestionId = "322456789123456789123456";
        InterviewQuestionUpdateDto updateDto = new InterviewQuestionUpdateDto(60);
        InterviewQuestionDocument updatetedInterviewQuestionDocument =
                createInterviewQuestionDocument(interviewQuestionId, userQuestionDocument, 60);
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(questionMapper.updateDocument(interviewQuestionDocument, updateDto)).thenReturn(updatetedInterviewQuestionDocument);
        when(interviewRepository.save(interviewDocument)).thenReturn(interviewDocument);
        when(questionMapper.toDto(updatetedInterviewQuestionDocument))
                .thenReturn(createInterviewQuestionDto(updatetedInterviewQuestionDocument));

        // when
        InterviewQuestionDto result = underTest.updateInterviewQuestion(interviewId, interviewQuestionId, updateDto);

        // then
        assertNotNull(result);
        assertEquals(1, interviewDocument.getQuestions().size());
        assertEquals("322456789123456789123456", result.getId());
        assertEquals(60, result.getGrade());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewRepository).save(interviewDocument);
        verify(questionMapper).toDto(updatetedInterviewQuestionDocument);
    }

    @Test
    @DisplayName("Should throw NoSuchEntityException when trying to delete question in interview that not exists")
    void shouldThrowNoSuchEntityExceptionWhenDeleteQuestionInInterviewThatNotExists() {
        // given
        String interviewId = "748456789123456789123456";
        String interviewQuestionId = "998456789123456789123456";
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.deleteInterviewQuestionById(interviewId, interviewQuestionId));
        verify(interviewRepository).findById(new ObjectId(interviewId));
    }

    @Test
    @DisplayName("Should delete interview question from interview")
    void shouldDeleteInterviewQuestion() {
        // given
        String interviewId = "748456789123456789123456";
        String interviewQuestionId = "322456789123456789123456";
        interviewDocument.setQuestions(new ArrayList<>(interviewDocument.getQuestions()));
        when(interviewRepository.findById(new ObjectId(interviewId))).thenReturn(Optional.of(interviewDocument));
        when(interviewRepository.save(interviewDocument)).thenReturn(interviewDocument);

        // when
        underTest.deleteInterviewQuestionById(interviewId, interviewQuestionId);

        // then
        assertEquals(0, interviewDocument.getQuestions().size());
        verify(interviewRepository).findById(new ObjectId(interviewId));
        verify(interviewRepository).save(interviewDocument);
    }

    private void matchInterviewFields(InterviewDto expected, InterviewDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getInterviewerId(), actual.getInterviewerId());
        assertEquals(expected.getCandidateId(), actual.getCandidateId());
        assertEquals(expected.getPlannedTime(), actual.getPlannedTime());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getQuestions(), actual.getQuestions());
    }
}
