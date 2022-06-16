package ru.ssk.restvoting.util.exception;

public class VoteNotFoundForUpdateException extends RuntimeException{
    public VoteNotFoundForUpdateException(String message) {
        super(message);
    }
}
