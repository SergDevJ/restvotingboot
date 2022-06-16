package ru.ssk.restvoting.util.exception;

public class VoteAlreadyExistException extends RuntimeException {
    public VoteAlreadyExistException(String message) {
        super(message);
    }
}
