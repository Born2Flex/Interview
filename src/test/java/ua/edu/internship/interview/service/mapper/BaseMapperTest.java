package ua.edu.internship.interview.service.mapper;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseMapperTest {
    private BaseMapper underTest;

    @BeforeEach
    public void setUp() {
        underTest = new BaseMapper();
    }

    @Test
    @DisplayName("Should map ObjectId to String with toString method")
    void shouldMapObjectIdToString() {
        // given
        ObjectId objectId = new ObjectId();

        // when
        String result = underTest.map(objectId);

        // then
        assertNotNull(result);
        assertEquals(objectId.toString(), result);
    }
}
