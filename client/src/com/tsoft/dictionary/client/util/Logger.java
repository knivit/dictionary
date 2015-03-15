package com.tsoft.dictionary.client.util;

import com.allen_sauer.gwt.log.client.Log;

public class Logger {
    private String className;

    public Logger(String className) {
        this.className = className;
    }

    public void debug(String methodName, String msg) {
        Log.debug(className + "." + methodName + "(): " + msg);
    }

    public void info(String methodName, String msg) {
        Log.info(className + "." + methodName + "(): " + msg);
    }

    public void error(String methodName, String msg) {
        Log.error(className + "." + methodName + "(): " + msg);
    }

    public void fatal(String methodName, String msg) {
        Log.fatal(className + "." + methodName + "(): " + msg);
    }
}
