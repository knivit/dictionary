package com.tsoft.ui.binder;

import java.lang.reflect.Method;

class InvokerException extends Exception {
    public InvokerException(Throwable cause) {
        super(cause);
    }

    public InvokerException(String message) {
        super(message);
    }

    public InvokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvokerException(Object instance, Method method, Throwable throwable) {
        super("Calling method \"" + method.getName() + "\"(...) of instance of class \"" + instance.getClass().getName() + "\"", throwable);
    }
}
