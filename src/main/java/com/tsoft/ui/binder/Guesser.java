package com.tsoft.ui.binder;

import java.lang.reflect.Method;

public class Guesser {
    public Method guessMethod(Object instance, String methodName, Class possibleEventArgumentClass) throws GuesserException {
        // case 1: try to retrieve method with one "possibleEventArgumentClass" argument
        try {
            Method logicActionMethod = instance.getClass().getMethod(methodName, possibleEventArgumentClass);
            logicActionMethod.setAccessible(true);
            return logicActionMethod;
        } catch (NoSuchMethodException e) {
            // do nothing: proceed to next case
        }

        // case 2: try to retrieve method with no arguments
        try {
            Method logicActionMethod = instance.getClass().getMethod(methodName);
            logicActionMethod.setAccessible(true);
            return logicActionMethod;
        } catch (NoSuchMethodException e) {
            throw new GuesserException("There is no action method \"" + methodName + "\" in class \"" + instance.getClass().getName() +
                    "\" which has either nor arguments nor 1 argument of class \"" + possibleEventArgumentClass.getName() + "\"");
        }
    }
}
