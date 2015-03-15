package com.tsoft.mvc;

import com.tsoft.ui.binder.Binder;
import javax.swing.JPanel;

public abstract class Form extends JPanel {
    protected View view;

    public abstract void createComponents();

    protected View getView(Class<? extends View> viewClass) {
        return getView().getMember(viewClass);
    }

    protected View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;

        Binder.bind(this);
    }

    public void close() {
        getView().getWindow().dispose();
    }
}
