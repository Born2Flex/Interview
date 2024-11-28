package ua.edu.internship.interview.service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidStateTransitionException extends BaseException {
    public InvalidStateTransitionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
