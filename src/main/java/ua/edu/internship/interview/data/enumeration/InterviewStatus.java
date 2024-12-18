package ua.edu.internship.interview.data.enumeration;

import ua.edu.internship.interview.service.utils.exceptions.InvalidStatusTransitionException;
import java.util.EnumSet;
import java.util.Set;

public enum InterviewStatus {
    PLANNED,
    ACTIVE,
    COMPLETED,
    CANCELLED;

    private Set<InterviewStatus> transitions;

    static {
        PLANNED.transitions = EnumSet.of(ACTIVE, CANCELLED);
        ACTIVE.transitions = EnumSet.of(COMPLETED, CANCELLED);
        COMPLETED.transitions = EnumSet.noneOf(InterviewStatus.class);
        CANCELLED.transitions = EnumSet.noneOf(InterviewStatus.class);
    }

    public Set<InterviewStatus> getTransitions() {
        return transitions;
    }

    public static void validateStatusTransition(InterviewStatus currentStatus, InterviewStatus newStatus) {
        if (!currentStatus.transitions.contains(newStatus)) {
            throw new InvalidStatusTransitionException("Invalid transition from " + currentStatus + " to " + newStatus);
        }
    }
}