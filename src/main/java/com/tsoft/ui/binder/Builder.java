package com.tsoft.ui.binder;

import java.lang.reflect.Proxy;

public class Builder {
    private Class listenerClass;

    private MutableInvocationHandler mutableInvocationHandler;

    public Builder( String listenerClassName ) throws BuilderException {
        this.listenerClass = getInterfaceClassByName( listenerClassName );
        this.mutableInvocationHandler = new MutableInvocationHandler();
    }

    public void bindListenerEventToLogicAction( String listenerEventMethodName, Invoker methodInvoker ) throws BuilderException {
        mutableInvocationHandler.bindListenerMapToActionInvoker( listenerEventMethodName, methodInvoker );
    }

    public Object getListener() {
        return Proxy.newProxyInstance( getClass().getClassLoader(),
                new Class[] { listenerClass }, mutableInvocationHandler );
    }

    private Class getInterfaceClassByName( String listenerClassName ) throws BuilderException {
        Class interfaceClass;
        try {
            interfaceClass = getClass().getClassLoader().loadClass( listenerClassName );
        } catch (ClassNotFoundException e) {
            throw new BuilderException(e);
        }

        if ( !interfaceClass.isInterface() ) {
            throw new BuilderException("Class \"" + listenerClassName + "\" should be interface.");
        }

        return interfaceClass;
    }
}