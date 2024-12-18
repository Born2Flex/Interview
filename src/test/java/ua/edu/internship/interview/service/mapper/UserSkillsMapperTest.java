package ua.edu.internship.interview.service.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.data.documents.UserSkillsDocument;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import ua.edu.internship.interview.service.dto.user.skill.UserSkillsDto;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserSkillsMapperTest {
    @Mock
    private SkillMapper skillMapper;
    @Mock
    private BaseMapper baseMapper;
    @InjectMocks
    private UserSkillsMapper underTest = new UserSkillsMapperImpl();

    @Test
    @DisplayName("Should map user skills document to user skills dtos")
    void shouldMapUserSkillsDocumentToUserSkillsDto() {
        // given
        UserSkillsDocument userSkillsDocument = createUserSkillsDocument();
        when(baseMapper.map(userSkillsDocument.getId())).thenReturn(userSkillsDocument.getId().toString());
        List<SkillDto> skillDtos = List.of(
                new SkillDto("123456789123456789123451","Java"),
                new SkillDto("123456789123456789123452", "Spring Boot")
        );
        when(skillMapper.toDto(userSkillsDocument.getSkills())).thenReturn(skillDtos);

        // when
        UserSkillsDto result = underTest.toDto(userSkillsDocument);

        // then
        assertNotNull(result);
        matchUserSkillsDocumentToDto(userSkillsDocument, result);
    }

    private UserSkillsDocument createUserSkillsDocument() {
        UserSkillsDocument userSkillsDocument = new UserSkillsDocument();
        userSkillsDocument.setId(new ObjectId("123456789123456789123456"));
        userSkillsDocument.setUserId(1L);

        ObjectId skillId1 = new ObjectId("123456789123456789123451");
        SkillDocument skill1 = new SkillDocument(skillId1, "Java", null);
        ObjectId skillId2 = new ObjectId("123456789123456789123452");
        SkillDocument skill2 = new SkillDocument(skillId2, "Spring Boot", null);

        userSkillsDocument.setSkills(List.of(skill1, skill2));
        return userSkillsDocument;
    }

    private void matchUserSkillsDocumentToDto(UserSkillsDocument document, UserSkillsDto dto) {
        assertEquals(document.getId().toString(), dto.getId());
        assertEquals(document.getUserId(), dto.getUserId());

        assertEquals(document.getSkills().size(), dto.getSkills().size());
        for (int i = 0; i < document.getSkills().size(); i++) {
            assertEquals(document.getSkills().get(i).getName(), dto.getSkills().get(i).getName());
        }
    }
}
