package com.tsoft.ui.binder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers listeners.
 *
 * Imagine, if "listenerInstance.getClass().getName()" return "java.awt.ActionListener"
 * then, the name of componentInstace's registration method will be: "add" + "ActionListener"
 * and the singature of the method will be "(" + listenerInstance.getClass() + ") : void"
 *
 * in other words, target call will look like (without reflection, sure)
 *
 * "componentInstance.addActionListener( listenerInstance )"
 *
 * NOTE: if there is no correct registration method, thow RegistrationException with
 *       corresponding message
 */
public class Registrator {
    public Registrator() {
    }

    public void register(Object componentInstance, Object listenerInstance) throws RegistratorException {
        // create map of "addXXXXList" methods names calculated from listener interfaces names
        Map<String, Class> listenerMethodToInterfaceMap = new HashMap<String, Class>();
        Class[] interfaces = listenerInstance.getClass().getInterfaces();
        for (Class interfaze : interfaces) {
            listenerMethodToInterfaceMap.put(calculateAddMethodName(interfaze.getName()), interfaze);
        }

        // go through all component's methods, check whether method is "listener registrator"
        // and if so, attach listener by calling this method
        for (Method method : componentInstance.getClass().getMethods()) {
            if (isMethodInListenerMethodToInterfaceNameMap(listenerMethodToInterfaceMap, method)) {
                attachListnerToComponent(method, componentInstance, listenerInstance);
                return;
            }
        }

        List<String> registrationMehods = new ArrayList<String>();
        for (Class interfaceClass : interfaces) {
            registrationMehods.add(calculateAddMethodName(interfaceClass.getName()) + "(...)");
        }

        throw new RegistratorException("Could not find none of registration methods " + registrationMehods + " in component \"" + componentInstance.getClass().getName() + "\"");
    }

    public void attachListnerToComponent(Method method, Object componentInstance, Object listenerInstance) throws RegistratorException {
        try {
            method.invoke(componentInstance, listenerInstance);
        } catch (IllegalAccessException e) {
            throw new RegistratorException(e);
        } catch (InvocationTargetException e) {
            throw new RegistratorException(e);
        }
    }

    public boolean isMethodInListenerMethodToInterfaceNameMap(Map<String, Class> listenerMethodToInterfaceNameMap, Method method) {
        if (!listenerMethodToInterfaceNameMap.containsKey(method.getName())) {
            return false;
        }

        if (method.getReturnType() != void.class) {
            return false;
        }

        if (method.getParameterTypes().length != 1) {
            return false;
        }

        Class parameterType = listenerMethodToInterfaceNameMap.get(method.getName());
        if (!parameterType.equals(method.getParameterTypes()[0])) {
            return false;
        }

        return true;
    }

    public String calculateAddMethodName(String interfaceClassName) {
        int pointIndex = interfaceClassName.lastIndexOf(".");

        String calculatedMethodName;

        if (pointIndex == -1) {
            calculatedMethodName = interfaceClassName;
        }

        calculatedMethodName = "add" + interfaceClassName.substring(pointIndex + 1);
        return calculatedMethodName;
    }
}
