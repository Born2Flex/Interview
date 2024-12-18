package ua.edu.internship.interview.utils;

import org.bson.types.ObjectId;
import ua.edu.internship.interview.data.documents.InterviewDocument;
import ua.edu.internship.interview.data.documents.InterviewQuestionDocument;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserQuestionDocument;
import ua.edu.internship.interview.data.enumeration.InterviewStatus;
import ua.edu.internship.interview.data.enumeration.QuestionDifficulty;
import ua.edu.internship.interview.data.enumeration.QuestionType;
import ua.edu.internship.interview.service.dto.interview.InterviewDto;
import ua.edu.internship.interview.service.dto.interview.question.InterviewQuestionDto;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionDto;
import ua.edu.internship.interview.service.dto.user.question.UserQuestionUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

public final class TestUtils {

    public static  SkillDto buildSkillDto(String id, String name) {
        return new SkillDto(id, name);
    }

    public static SkillDocument createSkillDocument(String skillId, String skillName) {
        return SkillDocument.builder()
                .id(new ObjectId(skillId))
                .name(skillName)
                .build();
    }

    public static SkillDto createSkillDto(SkillDocument skillDocument) {
        return SkillDto.builder()
                .id(skillDocument.getId().toString())
                .name(skillDocument.getName())
                .build();
    }

    public static UserQuestionUpdateDto createUserQuestionUpdateDto(String text, String skillId,
                                                                    QuestionDifficulty difficulty, QuestionType type) {
        return UserQuestionUpdateDto.builder()
                .text(text)
                .skillId(skillId)
                .difficulty(difficulty)
                .type(type).build();
    }

    public static UserQuestionDocument createUserQuestionDocument(String questionId, Long userId,
                                                                  SkillDocument skillDocument, String text,
                                                                  QuestionDifficulty difficulty,
                                                                  QuestionType questionType) {
        return UserQuestionDocument.builder()
                .id(new ObjectId(questionId))
                .skill(skillDocument)
                .text(text)
                .userId(userId)
                .difficulty(difficulty)
                .type(questionType)
                .build();
    }

    public static UserQuestionDto createUserQuestionDto(UserQuestionDocument userQuestionDocument) {
        return UserQuestionDto.builder()
                .id(userQuestionDocument.getUserId().toString())
                .text(userQuestionDocument.getText())
                .difficulty(userQuestionDocument.getDifficulty())
                .type(userQuestionDocument.getType())
                .userId(userQuestionDocument.getUserId())
                .skill(createSkillDto(userQuestionDocument.getSkill()))
                .build();
    }

    public static InterviewQuestionDocument createInterviewQuestionDocument(String questionId,
                                                                             UserQuestionDocument questionDocument,
                                                                             Integer grade) {
        return InterviewQuestionDocument.builder()
                .id(new ObjectId(questionId))
                .question(questionDocument)
                .grade(grade)
                .build();
    }

    public static InterviewQuestionDto createInterviewQuestionDto(InterviewQuestionDocument interviewQuestionDocument) {
        return InterviewQuestionDto.builder()
                .id(interviewQuestionDocument.getId().toString())
                .question(createUserQuestionDto(interviewQuestionDocument.getQuestion()))
                .grade(interviewQuestionDocument.getGrade())
                .build();
    }

    public static InterviewDocument createInterviewDocument(String interviewId, Long interviewerId, Long candidateId,
                                                            String title, InterviewStatus status,
                                                            List<InterviewQuestionDocument> questions) {
        return InterviewDocument.builder()
                .id(new ObjectId(interviewId))
                .interviewerId(interviewerId)
                .candidateId(candidateId)
                .title(title)
                .status(status)
                .plannedTime(LocalDateTime.now())
                .questions(questions)
                .build();
    }

    public static InterviewDto createInterviewDto(InterviewDocument interviewDocument) {
        return InterviewDto.builder()
                .id(interviewDocument.getId().toString())
                .interviewerId(interviewDocument.getInterviewerId())
                .candidateId(interviewDocument.getCandidateId())
                .title(interviewDocument.getTitle())
                .status(interviewDocument.getStatus())
                .plannedTime(interviewDocument.getPlannedTime())
                .startTime(interviewDocument.getStartTime())
                .endTime(interviewDocument.getEndTime())
                .feedback(interviewDocument.getFeedback())
                .questions(interviewDocument.getQuestions().stream().map(TestUtils::createInterviewQuestionDto).toList())
                .build();
    }
}
