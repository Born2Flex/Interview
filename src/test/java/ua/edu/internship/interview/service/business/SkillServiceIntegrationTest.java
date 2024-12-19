package ua.edu.internship.interview.service.business;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ua.edu.internship.interview.service.BaseIntegrationTest;
import ua.edu.internship.interview.service.dto.skill.SkillTreeDto;
import ua.edu.internship.interview.service.utils.exceptions.NoSuchEntityException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@Testcontainers
class SkillServiceIntegrationTest extends BaseIntegrationTest {
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.0"));
    @Autowired
    private SkillService skillService;

    public SkillServiceIntegrationTest(@Autowired MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.data.mongodb.database", () -> "testdb");
    }

    @BeforeEach
    void setUp() throws IOException {
        executeQueryFromFile("insert_skills.json");
    }

    @AfterEach
    void tearDown() throws IOException {
        executeQueryFromFile("delete_skills.json");
    }

    @Test
    @DisplayName("Should return all existing skill trees from DB")
    void shouldReturnAllSkillTrees() {
        // given
        // when
        List<SkillTreeDto> skillTrees = skillService.getAllSkillTrees();

        // then
        assertNotNull(skillTrees);
        assertEquals(3, skillTrees.size());
        assertEquals("Programming", skillTrees.getFirst().getName());
        assertEquals("Web Development", skillTrees.get(1).getName());
        assertEquals("Database Architecture", skillTrees.getLast().getName());
    }

    @Test
    @DisplayName("Should throw NoSuchEntity exception, when document with specified root id not found")
    void shouldThrowExceptionWhenNoDocumentWithSpecifiedRootId() {
        // given
        String invalidId = new ObjectId().toHexString();

        // when
        // then
        assertThrows(NoSuchEntityException.class, () -> skillService.getSkillTreeById(invalidId));
    }

    @Test
    @DisplayName("Should return skill tree by specified root id")
    void shouldReturnSkillTreeByRootId() {
        // given
        String objectId = "67483c5c8e8573107761acf3";

        // when
        SkillTreeDto retrievedSkillTree = skillService.getSkillTreeById(objectId);

        // then
        assertNotNull(retrievedSkillTree);
        assertEquals(objectId, retrievedSkillTree.getId());
        assertEquals("Database Architecture", retrievedSkillTree.getName());
    }
}
