package ua.edu.internship.interview.service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends BaseException {
    public InvalidStatusTransitionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
