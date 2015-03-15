package com.tsoft.mvc;

import java.util.ArrayList;
import java.util.Iterator;

public class MVCList implements Iterable<MVC> {
    private ArrayList<MVC> list = new ArrayList<MVC>();

    private ArrayList<MVC> getList() {
        return list;
    }

    public void add(MVC mvc) {
        getList().add(mvc);
    }

    public void addAll(MVCList list) {
        for (MVC mvc : list) {
            getList().add(mvc);
        }
    }
    
    public MVC get(Class<? extends Controller> controllerClass) {
        for (MVC mvc : getList()) {
            if (mvc.getController().getClass().equals(controllerClass)) {
                return mvc;
            }
        }

        return null;
    }

    public View getView(Class<? extends View> viewClass) {
        for (MVC mvc : getList()) {
            if (mvc.getView().getClass().equals(viewClass)) {
                return mvc.getView();
            }
        }

        throw new RuntimeException("View '" + viewClass.getName() + "' not found");
    }

    public void clear() {
        getList().clear();
    }

    @Override
    public Iterator<MVC> iterator() {
        return getList().iterator();
    }
}
