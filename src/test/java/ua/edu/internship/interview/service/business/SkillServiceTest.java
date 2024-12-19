package ua.edu.internship.interview.service.business;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.edu.internship.interview.data.repository.SkillRepository;
import ua.edu.internship.interview.service.dto.skill.SkillTreeDto;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private SkillService underTest;

    @Test
    @DisplayName("Should return all existing skill trees from DB")
    void shouldReturnAllSkillTrees() {
        // given
        SkillTreeDto tree1 = new SkillTreeDto();
        tree1.setId("tree1");
        tree1.setName("tree 1");
        SkillTreeDto tree2 = new SkillTreeDto();
        tree2.setId("tree2");
        tree2.setName("tree 2");
        when(skillRepository.findSkillTrees()).thenReturn(List.of(tree1, tree2));

        // when
        List<SkillTreeDto> result = underTest.getAllSkillTrees();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("tree1", result.get(0).getId());
        assertEquals("tree2", result.get(1).getId());
        verify(skillRepository).findSkillTrees();
    }

    @Test
    @DisplayName("Should return skill tree by specified root id")
    void shouldReturnSkillTreeByRootId() {
        // given
        String skillId = "rootId";
        SkillTreeDto rootTree = getMockSkillTree();
        when(skillRepository.findSkillTreeById(skillId)).thenReturn(Optional.of(rootTree));

        // when
        SkillTreeDto result = underTest.getSkillTreeById(skillId);

        // then
        assertNotNull(result);
        assertEquals(skillId, result.getId());
        assertEquals("root skill", result.getName());
        assertEquals(2, result.getChildren().size());
        assertEquals("childId1", result.getChildren().get(0).getId());
        assertEquals("childId2", result.getChildren().get(1).getId());
        verify(skillRepository).findSkillTreeById(skillId);
    }

    @Test
    @DisplayName("Should throw NoSuchEntity exception, when document with specified root id not found")
    void shouldThrowExceptionWhenNoDocumentWithSpecifiedRootId() {
        // given
        String skillId = "aaabbbccc";
        when(skillRepository.findSkillTreeById(skillId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> underTest.getSkillTreeById(skillId));
        verify(skillRepository).findSkillTreeById(skillId);
    }

    @Test
    @DisplayName("Should return empty list when there are no skill trees")
    void shouldReturnEmptyCollectionWhenNoSkillTrees() {
        // given
        when(skillRepository.findSkillTrees()).thenReturn(List.of());

        // when
        List<SkillTreeDto> result = underTest.getAllSkillTrees();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(skillRepository).findSkillTrees();
    }

    private SkillTreeDto getMockSkillTree() {
        SkillTreeDto child1 = new SkillTreeDto();
        child1.setId("childId1");
        child1.setName("child skill 1");
        child1.setParentId("rootId");
        SkillTreeDto child2 = new SkillTreeDto();
        child2.setId("childId2");
        child2.setName("child skill 2");
        child2.setParentId("rootId");
        SkillTreeDto rootTree = new SkillTreeDto();
        rootTree.setId("rootId");
        rootTree.setName("root skill");
        rootTree.setChildren(List.of(child1, child2));
        return rootTree;
    }
}
