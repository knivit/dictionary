package com.tsoft.ui.binder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MutableInvocationHandler implements InvocationHandler {
    private final Logger logger = Logger.getLogger(MutableInvocationHandler.class.getName());

    private Map<String, Invoker> listenerEventToActionInvokerMap;

    public MutableInvocationHandler() {
        this.listenerEventToActionInvokerMap = new HashMap<String, Invoker>();
    }

    public void bindListenerMapToActionInvoker(String listenerEvent, Invoker actionInvoker) throws IllegalArgumentException {
        if (listenerEventToActionInvokerMap.containsKey(listenerEvent)) {
            logger.log(Level.SEVERE, "Listener event \"" + listenerEvent + "\" is already registered");
            return;
        }

        listenerEventToActionInvokerMap.put(listenerEvent, actionInvoker);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String listenerEvent = method.getName();

        // check, whether binding exists. If so, force action call
        if (listenerEventToActionInvokerMap.containsKey(listenerEvent)) {
            Invoker actionInvoker = (Invoker) listenerEventToActionInvokerMap.get(listenerEvent);
            return actionInvoker.invoke(args);
        }

        // silentlty ignore all calls
        return null;
    }
}
