package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.documents.SkillDocument;
import ua.edu.internship.interview.service.dto.skill.SkillDto;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillMapperTest {
    @Mock
    private BaseMapper baseMapper;
    @InjectMocks
    private SkillMapper underTest = new SkillMapperImpl();

    @Test
    @DisplayName("Should map skill document to skill dto")
    void shouldMapSkillDocumentToDTO() {
        // given
        String skillId = "507f191e810c19729de860ea";
        ObjectId skillObjectId = new ObjectId(skillId);
        String skillName = "Network Fundamentals";
        SkillDocument skillDocument = createSkillDocument(skillId, skillName);
        when(baseMapper.map(skillObjectId)).thenReturn(skillId);

        // when
        SkillDto skillDto = underTest.toDto(skillDocument);

        // then
        assertEquals(skillObjectId.toString(), skillDto.getId());
        assertEquals(skillName, skillDto.getName());
    }

    @Test
    @DisplayName("Should map list of skill documents to list of skill dtos")
    void shouldMapListOfSkillDocumentsToListOfSkillDTOs() {
        // given
        String skillId1 = "507f191e810c19729de860ea";
        String skillName1 = "Programming Languages";
        SkillDocument skillDocument1 = createSkillDocument(skillId1, skillName1);
        when(baseMapper.map(new ObjectId(skillId1))).thenReturn(skillId1);
        String skillId2 = "507f191e810c19729de860eb";
        String skillName2 = "Compiler Architecture";
        SkillDocument skillDocument2 = createSkillDocument(skillId2, skillName2);
        when(baseMapper.map(new ObjectId(skillId2))).thenReturn(skillId2);
        List<SkillDocument> skillDocuments = List.of(skillDocument1, skillDocument2);

        // when
        List<SkillDto> skillDtos = underTest.toDto(skillDocuments);

        // then
        assertEquals(2, skillDtos.size());
        matchSkillFields(new SkillDto(skillId1, skillName1), skillDtos.getFirst());
        matchSkillFields(new SkillDto(skillId2, skillName2), skillDtos.getLast());
    }

    private SkillDocument createSkillDocument(String skillId, String skillName) {
        SkillDocument skillDocument = new SkillDocument();
        skillDocument.setId(new ObjectId(skillId));
        skillDocument.setName(skillName);
        return skillDocument;
    }

    private void matchSkillFields(SkillDto expected, SkillDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
    }
}
