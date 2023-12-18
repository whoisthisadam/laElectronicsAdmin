package com.kasperovich.laelectronics.exception;

public class NotDeletableStatusException extends Exception{

    public NotDeletableStatusException() {
    }

    public NotDeletableStatusException(String message) {
        super(message);
    }

    public NotDeletableStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotDeletableStatusException(Throwable cause) {
        super(cause);
    }

    public NotDeletableStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
