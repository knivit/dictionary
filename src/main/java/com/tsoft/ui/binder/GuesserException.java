package com.tsoft.ui.binder;

public class GuesserException extends Exception {
    public GuesserException(Throwable cause) {
        super(cause);
    }

    public GuesserException(String message) {
        super(message);
    }

    public GuesserException(String message, Throwable cause) {
        super(message, cause);
    }
}
