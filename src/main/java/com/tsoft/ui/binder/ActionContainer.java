package com.tsoft.ui.binder;

public abstract class ActionContainer {
    private Action action;

    public ActionContainer() {
    }

    public boolean hasAction() {
        return true;
    }

    public Action getAction() {
        return action;
    }

    void setAction(Action action) {
        this.action = action;
    }
}
