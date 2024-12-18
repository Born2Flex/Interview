package ua.edu.internship.interview.data.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ua.edu.internship.interview.service.utils.exceptions.InvalidStateTransitionException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InterviewStatusTest {
    @ParameterizedTest
    @EnumSource(InterviewStatus.class)
    @DisplayName("Should not throw exception when there is a valid interview status transition")
    void shouldReturnTrueForValidTransitions(InterviewStatus currentStatus) {
        for (InterviewStatus newStatus : currentStatus.getTransitions()) {
            assertDoesNotThrow(() -> InterviewStatus.validateStatusTransition(currentStatus, newStatus));
        }
    }

    @ParameterizedTest
    @EnumSource(InterviewStatus.class)
    @DisplayName("Should throw exception when trying to perform invalid interview status transition")
    void shouldReturnFalseForInvalidTransitions(InterviewStatus currentStatus) {
        for (InterviewStatus newStatus : InterviewStatus.values()) {
            if (!currentStatus.getTransitions().contains(newStatus)) {
                assertThrows(InvalidStateTransitionException.class,
                        () -> InterviewStatus.validateStatusTransition(currentStatus, newStatus));
            }
        }
    }
}
