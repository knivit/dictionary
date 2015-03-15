package com.tsoft.ui.binder;

public class ConfigException extends Exception {
    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void doThrow(String message, Class guiClass) throws ConfigException {
        throw new ConfigException(createMessage(message, guiClass, null, null, null));
    }

    public static void doThrow(String message, Class guiClass, String component, Class listenerType) throws ConfigException {
        throw new ConfigException(createMessage(message, guiClass, component, listenerType, null));
    }

    public static void doThrow(String message, Class guiClass, String component, Class listenerType, String mapping) throws ConfigException {
        throw new ConfigException(createMessage(message, guiClass, component, listenerType, mapping));
    }

    static String createMessage(String message, Class guiClass, String component, Class listenerType, String mapping) {
        return message + ". Gui class \"" + guiClass.getName() + "\"" +
                (component != null ? ", component \"" + component + "\"" : "") +
                (listenerType != null ? ", listenerType \"" + listenerType.getName() + "\"" : "") +
                (mapping != null ? ", mapping \"" + mapping + "\"" : "") +
                ".";
    }
}
