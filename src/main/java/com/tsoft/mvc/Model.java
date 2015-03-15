package com.tsoft.mvc;

public abstract class Model {
    protected Controller controller;

    private Model parentModel;

    public abstract void init() throws Exception;

    public abstract void close();

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Model getParentModel() {
        return parentModel;
    }
    
    public void setParentModel(Model parentModel) {
        this.parentModel = parentModel;
    }
}
