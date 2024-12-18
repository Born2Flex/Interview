package ua.edu.internship.interview.service.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserSkillsDocument;
import ua.edu.internship.interview.data.repository.SkillRepository;
import ua.edu.internship.interview.data.repository.UserSkillRepository;
import ua.edu.internship.interview.service.client.UserServiceClient;
import ua.edu.internship.interview.service.dto.user.skill.UserSkillsDto;
import ua.edu.internship.interview.service.mapper.UserSkillsMapper;
import ua.edu.internship.interview.service.utils.exceptions.InvalidInputException;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSkillsService {
    private final UserSkillRepository userSkillRepository;
    private final SkillRepository skillRepository;
    private final UserSkillsMapper mapper;
    private final UserServiceClient userClient;

    public UserSkillsDto getUserSkills(Long userId) {
        UserSkillsDocument userSkillsDocument = getSkillsByUserIdOrElseThrow(userId);
        log.info("Retrieved {} skills for user with id: {}", userSkillsDocument.getSkills().size(), userId);
        return mapper.toDto(userSkillsDocument);
    }

    public UserSkillsDto createUserSkills(Long userId, List<String> skillIds) {
        log.info("Attempting to create skills document for user with id: {}", userId);
        validateUserExistsById(userId);
        validateUserSkillsNotExists(userId);
        List<SkillDocument> skills = getSkillsByIds(skillIds);
        UserSkillsDocument userSkillsDocument = createUserSkillsDocument(userId, skills);
        UserSkillsDocument savedUserSkills = userSkillRepository.save(userSkillsDocument);
        log.info("Created skills document with id: {}, for user with id: {}", savedUserSkills.getId(), userId);
        return mapper.toDto(savedUserSkills);
    }

    public UserSkillsDto updateUserSkills(Long userId, List<String> skillIds) {
        log.info("Attempting to update skills for user with id: {}", userId);
        UserSkillsDocument userSkillsDocument = getSkillsByUserIdOrElseThrow(userId);
        List<SkillDocument> skills = getSkillsByIds(skillIds);
        userSkillsDocument.setSkills(skills);
        UserSkillsDocument updatedUserSkills = userSkillRepository.save(userSkillsDocument);
        log.info("Updated skills for user with id: {}", userId);
        return mapper.toDto(updatedUserSkills);
    }

    private UserSkillsDocument createUserSkillsDocument(Long userId, List<SkillDocument> skills) {
        return UserSkillsDocument.builder()
                .userId(userId)
                .skills(skills)
                .build();
    }

    private void validateUserSkillsNotExists(Long userId) {
        userSkillRepository.findByUserId(userId).ifPresent(this::throwInvalidInputException);
    }

    private void throwInvalidInputException(UserSkillsDocument document) {
        throw new InvalidInputException("User skills document already exists for this user");
    }

    private List<SkillDocument> getSkillsByIds(List<String> skillIds) {
        List<SkillDocument> skills = skillRepository.findAllById(skillIds.stream().map(ObjectId::new).toList());
        if (skills.size() != skillIds.size()) {
            throw new InvalidInputException("Provided invalid skill id");
        }
        return skills;
    }

    private UserSkillsDocument getSkillsByUserIdOrElseThrow(Long userId) {
        return userSkillRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchEntityException("User skills not found by user id: " + userId));
    }

    private void validateUserExistsById(Long userId) {
        if (!userClient.existsById(userId)) {
            throw new NoSuchEntityException("User not found by id: " + userId);
        }
    }
}
