package ru.ssk.restvoting.util.exception;

public class UserDeleteViolationException extends RuntimeException {
    public UserDeleteViolationException(String message) {
        super(message);
    }
}
