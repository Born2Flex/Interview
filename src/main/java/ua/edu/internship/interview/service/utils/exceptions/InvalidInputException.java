package ua.edu.internship.interview.service.utils.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends BaseException {
    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
