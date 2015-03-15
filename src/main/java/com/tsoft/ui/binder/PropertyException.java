package com.tsoft.ui.binder;

public class PropertyException extends Exception {
    public PropertyException(Object o, String property, Throwable cause) {
        super("Instance of class \"" + o.getClass().getName() + "\", property \"" + property + "\"", cause);
    }

    public PropertyException(Object o, String property, String message) {
        super("Instance of class \"" + o.getClass().getName() + "\", property \"" + property + "\". " + message);
    }

    public PropertyException(Object o, String property, String message, Throwable cause) {
        super("Instance of class \"" + o.getClass().getName() + "\", property \"" + property + "\". " + message, cause);
    }
}
