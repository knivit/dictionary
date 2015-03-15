package com.tsoft.ui.binder;

public class Action extends ActionContainer {
    private String method;

    private Action() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static Action logic() {
        return new LogicAction();
    }

    public static Action condition() {
        return new Action();
    }

    private static class LogicAction extends Action {
        private LogicAction() {
        }

        @Override
        public boolean hasAction() {
            return false;
        }

        @Override
        public Action getAction() {
            throw new UnsupportedOperationException("Nested action is not supported");
        }

        @Override
        public void setAction(Action action) {
            throw new UnsupportedOperationException();
        }
    }
}
