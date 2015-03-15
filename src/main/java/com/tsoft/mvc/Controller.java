package com.tsoft.mvc;

public abstract class Controller {
    protected Model model;
    protected View view;

    private Controller parentController;

    private MVCList memberList = new MVCList();

    public abstract void init() throws Exception;

    protected void addMember(Class<? extends Controller> controllerClass, Class<? extends Model> modelClass, Class<? extends View> viewClass) throws Exception {
        MVC mvc = MVC.newInstance(controllerClass, modelClass, viewClass);
        getMemberList().add(mvc);

        mvc.getController().setParentController(this);
    }

    public MVC getMember(Class<? extends Controller> controllerClass) {
        MVC mvc = getMemberList().get(controllerClass);
        if (mvc == null) {
            throw new RuntimeException("Controller '" + controllerClass.getName() + "' not found");
        }
        return mvc;
    }

    public static void addSingleton(Class<? extends Controller> controllerClass, Class<? extends Model> modelClass, Class<? extends View> viewClass) throws Exception {
        MVC.addSingleton(controllerClass, modelClass, viewClass);
    }

    public MVC getSingleton(Class<? extends Controller> controllerClass) {
        return MVC.findSingleton(controllerClass);
    }

    public View getMemberView(Class<? extends View> viewClass) {
        View memberView = getMemberList().getView(viewClass);
        if (memberView == null) {
            throw new RuntimeException("View '" + viewClass.getName() + "' not found");
        }
        return memberView;
    }

    public void viewClosed() {
        for (MVC member : getMemberList()) {
            member.getController().viewClosed();
        }

        getModel().close();

        // close the singletons when the parent controller closing
        if (getParentController() == null) {
            MVC.viewClosed();
        }
    }

    private MVCList getMemberList() {
        return memberList;
    }

    protected Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    protected View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Controller getParentController() {
        return parentController;
    }

    public void setParentController(Controller parentController) {
        this.parentController = parentController;

        getView().setParentView(getParentController() == null ? null : getParentController().getView());
        getModel().setParentModel(getParentController() == null ? null : getParentController().getModel());
    }
}
