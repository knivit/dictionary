package com.tsoft.mvc;

public class MVC {
    private Controller controller;
    private Model model;
    private View view;

    // Singleton MVC objects of the App
    private static MVCList singletonList = new MVCList();

    public static MVC newInstance(Class<? extends Controller> controllerClass, Class<? extends Model> modelClass, Class<? extends View> viewClass) throws Exception {
        Controller controller = controllerClass.newInstance();
        Model model = modelClass.newInstance();
        View view = viewClass.newInstance();

        controller.setModel(model);
        controller.setView(view);
        model.setController(controller);
        view.setController(controller);

        controller.init();
        model.init();
        view.init();

        MVC mvc = new MVC(controller, model, view);
        return mvc;
    }

    public MVC(Controller controller, Model model, View view) {
        this.controller = controller;
        this.model = model;
        this.view = view;
    }

    public void run(Controller parentController, boolean isModal) throws Exception {
        getController().setParentController(parentController);
        getView().run(isModal);
    }

    public void run(boolean isModal) throws Exception {
        run(null, isModal);
    }

    public Controller getController() {
        return controller;
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }
    
    private static MVCList getSingletonList() {
        return singletonList;
    }

    public static void addSingleton(Class<? extends Controller> controllerClass, Class<? extends Model> modelClass, Class<? extends View> viewClass) throws Exception {
        MVC mvc = findSingleton(controllerClass);
        if (mvc != null) {
            return;
        }

        mvc = MVC.newInstance(controllerClass, modelClass, viewClass);
        getSingletonList().add(mvc);
    }

    public static MVC findSingleton(Class<? extends Controller> controllerClass) {
        return getSingletonList().get(controllerClass);
    }


    public static void viewClosed() {
        MVCList copyList = new MVCList();
        copyList.addAll(getSingletonList());

        // clear the singleton list to prevent a recursion
        getSingletonList().clear();
        
        for (MVC mvc : copyList) {
            mvc.getController().viewClosed();
        }
    }
}
