package com.tsoft.ui.binder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {
    private List<Listener> listeners = new ArrayList<Listener>();
    private Class objClass;

    public List<Listener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    private void addListener(Listener listener) {
        listeners.add(listener);
    }

    public Config(Class objClass) throws ConfigException {
        this.objClass = objClass;

        List<Field> fieldList = Arrays.asList(objClass.getDeclaredFields());

        for (Field field : fieldList) {
            if (field.isAnnotationPresent(EventListeners.class)) {
                for (EventListener listenerAnn : field.getAnnotation(EventListeners.class).value()) {
                    addListener(objClass, field, listenerAnn);
                }

                continue;
            }

            if (field.isAnnotationPresent(EventListener.class)) {
                addListener(objClass, field, field.getAnnotation(EventListener.class));
            }
        }
    }

    private void addListener(Class guiClass, Field field, EventListener listenerAnn) throws ConfigException {
        String component = field.getName();
        if (listenerAnn.property().length() != 0) {
            component = component + "." + listenerAnn.property();
        }

        addListener(createListener(component, listenerAnn.type(), listenerAnn.mappings(), guiClass));
    }

    private Listener createListener(String component, Class listenerType, String[] mappings, Class guiClass) throws ConfigException {
        Listener listener = new Listener();
        listener.setComponent(component);
        listener.setType(listenerType.getName());

        if (mappings.length == 0) {
            ConfigException.doThrow("There are no any mappings", guiClass, component, listenerType);
        }

        for (String mapping : mappings) {
            String[] mappingTokens = mapping.split("\\>");

            if (mappingTokens.length < 2) {
                ConfigException.doThrow("There are " + mappingTokens.length + " item(s) in mapping. Should be 2 at least (e.g. \"actionPerformed > show\")",
                        guiClass, component, listenerType, mapping);
            }

            listener.addEvent(createEvent(mappingTokens));
        }

        return listener;
    }

    private Event createEvent(String[] mappingTokens) {
        Event event = null;
        ActionContainer lastActionContainer = null;
        for (int i = 0; i < mappingTokens.length; i++) {
            String mappingToken = mappingTokens[i].trim();

            if (i == 0) {
                event = new Event();
                event.setMethod(mappingToken);
                lastActionContainer = event;
            } else {
                Action action;
                if (i == mappingTokens.length - 1) {
                    action = Action.logic();
                } else {
                    action = Action.condition();
                }

                action.setMethod(mappingToken);
                lastActionContainer.setAction(action);
                lastActionContainer = action;
            }
        }
        
        return event;
    }

    @Override
    public String toString() {
        return "Config{" + objClass + '}';
    }
}
