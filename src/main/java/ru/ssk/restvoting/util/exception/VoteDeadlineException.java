package ru.ssk.restvoting.util.exception;

public class VoteDeadlineException extends RuntimeException {
    public VoteDeadlineException(String msg) {
        super(msg);
    }
}
