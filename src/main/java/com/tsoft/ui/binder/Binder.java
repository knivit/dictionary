package com.tsoft.ui.binder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Binder {
    private Config config;
    private Object gui;
    private Object logic;
    private Guesser guesser;

    public static void bind(Object o) throws RuntimeException {
        try {
            Config config = new Config(o.getClass());
            Binder binder = new Binder(config, o);
            binder.bind();
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        } catch (BinderException e) {
            throw new RuntimeException(e);
        }
    }

    public Binder(Config config, Object guiAndLogic) {
        this(config, guiAndLogic, guiAndLogic);
    }

    public Binder(Config config, Object gui, Object logic) {
        this.config = config;
        this.gui = gui;
        this.logic = logic;

        this.guesser = new Guesser();
    }

    public void bind() throws BinderException {
        bindListeners();
    }

    private void bindListeners() throws BinderException {
        for (Listener listener : config.getListeners()) {
            // extract component from GUI by its specification
            Object componentInstance;
            try {
                componentInstance = Property.getValue(gui, listener.getComponent());
            } catch (PropertyException e) {
                throw new BinderException(config, e);
            }

            if (componentInstance == null) {
                throw new BinderException(config, "Got null for component \"" + listener.getComponent() +
                        "\", gui class \"" + gui.getClass().getName() + "\"");
            }

            // create proxy-listener and tune listeners
            Builder builder;
            try {
                builder = new Builder(listener.getType());
            } catch (BuilderException e) {
               throw new BinderException(config, e);
            }

            // go through all event's and weave them into the listener (which is under construction)
            Class listenerClass = getInterfaceClassByName(listener.getType());
            for (Event event : listener.getEvents()) {
                addEventToBuilder(builder, listener, event, extractPossibleEventArgumentClass(listenerClass, event.getMethod()));
            }

            Object listenerInstance = builder.getListener();

            // register listener in the component
            Registrator registrator = new Registrator();
            try {
                registrator.register(componentInstance, listenerInstance);
            } catch (RegistratorException e) {
                throw new BinderException(config, e);
            }
        }
    }

    private void addEventToBuilder(Builder builder, Listener listener, Event event, Class possibleEventArgumentClass) throws BinderException {
        Invoker methodInvoker = null;
        try {
            methodInvoker = buildMethodInvokerFromGenericAction(event.getAction(), possibleEventArgumentClass);
        } catch (GuesserException e) {
            throw new BinderException(config, e);
        }

        try {
            builder.bindListenerEventToLogicAction(event.getMethod(), methodInvoker);
        } catch (BuilderException e) {
            throw new BinderException(config, "Error occured when tried to bind gui event method \"" + event.getMethod() + "\" for component \"" + listener.getComponent() + "\"", e);
        }
    }

    private Invoker buildMethodInvokerFromGenericAction(Action action, Class possibleEventArgumentClass) throws GuesserException {
        // TODO refactor with usage of Visitor (applied to IAction)

        // process condition (gui)
        if (action.hasAction()) {
            return invokeCondition(
                    gui, guesser.guessMethod(gui, action.getMethod(), possibleEventArgumentClass),
                    buildMethodInvokerFromGenericAction(action.getAction(), possibleEventArgumentClass));
        }

        // process action (logic)
        return invokeAction(
                logic, guesser.guessMethod(logic, action.getMethod(), possibleEventArgumentClass));
    }

    private Class extractPossibleEventArgumentClass(Class listenerClass, String listenerEventMethodName) throws BinderException {
        Method listenerEventMethod = findListenerEventMethod(listenerClass, listenerEventMethodName);
        return listenerEventMethod.getParameterTypes()[0];
    }

    private Method findListenerEventMethod(Class listenerClass, String listenerEventMethodName) throws BinderException {
        for (Method method : listenerClass.getMethods()) {
            if (listenerEventMethodName.equals(method.getName())) {
                if (method.getParameterTypes().length != 1) {
                    throw new BinderException(config, "There is a listener event method with name \"" + listenerEventMethodName + "\", but number of parameters don't march: expected 1, actual" + method.getParameterTypes().length);
                }

                return method;
            }
        }
        
        throw new BinderException(config, "There is no listener event method with name \"" + listenerEventMethodName + "\"");
    }

    private Class getInterfaceClassByName(String listenerClassName) throws BinderException {
        Class interfaceClass;
        try {
            interfaceClass = getClass().getClassLoader().loadClass(listenerClassName);
        } catch (ClassNotFoundException e) {
            throw new BinderException(config, e);
        }

        if (!interfaceClass.isInterface()) {
            throw new BinderException(config, "Class \"" + listenerClassName + "\" should be interface.");
        }

        return interfaceClass;
    }

    private Invoker invokeAction(Object instance, Method actionMethod) {
        return new ActionMethodInvoker(instance, actionMethod);
    }

    private Invoker invokeCondition(Object instance, Method actionMethod, Invoker delegateMethodInvoker) {
        return new ConditionMethodInvoker(instance, actionMethod, delegateMethodInvoker);
    }

    private static class ActionMethodInvoker implements Invoker {

        private Object instance;
        private Method method;

        public ActionMethodInvoker(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }

        @Override
        public Object invoke(Object... args) throws InvokerException {
            // adjust arguments for no-arg case
            if (method.getParameterTypes().length == 0) {
                args = new Object[]{};
            }

            // invoke
            try {
                return method.invoke(instance, args);
            } catch (IllegalAccessException e) {
                throw new InvokerException(instance, method, e);
            } catch (InvocationTargetException e) {
                throw new InvokerException(instance, method, e);
            }
        }
    }

    private static class ConditionMethodInvoker implements Invoker {

        private Object instance;
        private Method method;
        private Invoker delegateMethodInvoker;

        public ConditionMethodInvoker(Object instance, Method method, Invoker delegateMethodInvoker) {
            // check method returns boolean
            if (!Boolean.class.equals(method.getReturnType()) && !boolean.class.equals(method.getReturnType())) {
                throw new RuntimeException("Method \"" + method.getName() + "(...)\" of class \"" + instance.getClass().getName() + "\" expected to have return value of type \"" + Boolean.class.getName() + "\" or \"boolean\", but found type of return value is \"" + method.getReturnType().getName() + "\"");
            }

            this.instance = instance;
            this.method = method;
            this.delegateMethodInvoker = delegateMethodInvoker;
        }

        @Override
        public Object invoke(Object... args) throws InvokerException {
            // adjust arguments for no-arg case
            if (method.getParameterTypes().length == 0) {
                args = new Object[]{};
            }

            // invoke condition
            Boolean isConditionPassed;
            try {
                isConditionPassed = (Boolean) method.invoke(instance, args);
            } catch (IllegalAccessException e) {
                throw new InvokerException(e);
            } catch (InvocationTargetException e) {
                throw new InvokerException(e);
            }

            // invoke nested action with same arguments if condition is passed
            if (isConditionPassed.booleanValue()) {
                return delegateMethodInvoker.invoke(args);
            }

            // do nothing
            return null;
        }
    }
}
