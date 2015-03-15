package com.tsoft.mvc;

import java.awt.BorderLayout;
import java.awt.Window;

public class ViewRunner implements Runnable {
    private View view;
    private Window window;
    private Form form;

    public ViewRunner(View view, Window window, Form form) {
        this.view = view;
        this.window = window;
        this.form = form;
    }

    @Override
    public void run() {
        window.add(form, BorderLayout.CENTER);
        window.pack();

        window.setLocationRelativeTo(view.getParentWindow());

        window.setVisible(true);
    }
}
