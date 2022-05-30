package ru.ssk.restvoting.util.exception;

import org.springframework.http.HttpStatus;

public class ErrorInfo {
    private final CharSequence url;
    private final String messageText;
    private final String[] details;
    private final HttpStatus status;

    public ErrorInfo(CharSequence url, String messageText, String[] details, HttpStatus status) {
        this.url = url;
        this.messageText = messageText;
        this.details = details;
        this.status = status;
    }

    public CharSequence getUrl() {
        return url;
    }

    public String getMessageText() {
        return messageText;
    }

    public String[] getDetails() {
        return details;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
