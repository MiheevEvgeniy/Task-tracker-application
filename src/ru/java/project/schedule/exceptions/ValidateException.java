package ru.java.project.schedule.exceptions;


public class ValidateException extends RuntimeException {
    public ValidateException() {
    }

    public ValidateException(final String message) {
        super(message);
    }

    public ValidateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ValidateException(final Throwable cause) {
        super(cause);
    }
}