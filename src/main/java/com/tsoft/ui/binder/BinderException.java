package com.tsoft.ui.binder;

public class BinderException extends Exception {
    public BinderException(Config config, String message) {
        super(buildExceptionMessage(config, message));
    }

    public BinderException(Config config, Throwable cause) {
        this(config, "", cause);
    }

    public BinderException(Config config, String message, Throwable cause) {
        super(buildExceptionMessage(config, message), cause);
    }

    private static String buildExceptionMessage(Config config, String message) {
        // TODO introduce config.getSource()
        return "Config \"" + config + "\". " + message;
    }
}
