package ru.ssk.restvoting.util.exception;

public class TooLateVoteException extends RuntimeException {
    public TooLateVoteException(String msg) {
        super(msg);
    }
}
