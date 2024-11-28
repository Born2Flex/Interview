package ua.edu.internship.interview.service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class InterviewCollisionException extends BaseException {
    public InterviewCollisionException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
