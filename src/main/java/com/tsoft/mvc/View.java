package com.tsoft.mvc;

import com.tsoft.ui.binder.Binder;
import java.awt.Dialog;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public abstract class View {
    private Logger logger = Logger.getLogger(View.class.getName());
    
    protected Controller controller;
    private View parentView;
    
    private Window window;

    public abstract Class<? extends Form> getMainFormClass();

    public abstract String getCaption();

    public void init() throws Exception { }
    
    public void run(boolean isModal) {
        try {  
            // use the platform look and feel  
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
        } catch(Exception e) {
        }  

        Binder.bind(this);

        window = createWindow(getCaption(), isModal);
        Form form = createMainForm();
        prepareWindow();

        ViewRunner viewRunner = new ViewRunner(this, window, form);
        SwingUtilities.invokeLater(viewRunner);
    }

    public void prepareWindow() {
        if (getTopWindow() != null) {
            return;
        }

        // close the app in a form standalone mode
        getWindow().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getController().viewClosed();
                System.exit(0);
            }
        });
    }

    public View getMember(Class<? extends View> viewClass) {
        return getController().getMemberView(viewClass);
    }

    private Window createWindow(String caption, boolean isModal) {
        if (isModal) {
            window = createModalWindow(caption);
        } else {
            window = createFrameWindow(caption);
        }

        return window;
    }

    private Window createModalWindow(String caption) {
        JDialog dialog = new JDialog();
        dialog.setTitle(caption);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        return dialog;
    }

    private Window createFrameWindow(String caption) {
        JFrame frame = new JFrame(caption);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    public Form createMainForm() {
        return createForm(getMainFormClass());
    }

    protected Form createForm(Class<? extends Form> formClass) {
        try {
            Form form = formClass.newInstance();
            form.setView(this);

            form.createComponents();

            return form;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't create form " + formClass.getName(), ex);
            System.exit(-1);
        }

        throw new RuntimeException();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Window getWindow() {
        return window;
    }

    public InputStream getResource(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }

    public Image getImage(String fileName) {
        fileName = "/icons/" + fileName;
        InputStream is = getResource(fileName);
        if (is == null) {
            logger.log(Level.SEVERE, "File '" + fileName + "' not found");
            return null;
        }

        try {
            return ImageIO.read(is);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "File '" + fileName + "' not found", ex);
        }

        return null;
    }

    public Window getTopWindow() {
        return getParentWindow(0);
    }

    public Window getParentWindow() {
        return getParentView() == null ? null : getParentView().getParentWindow();
    }

    private Window getParentWindow(int level) {
        if (level == 0) {
            return getParentView() == null ? null : getParentView().getParentWindow(level + 1);
        }

        return getParentView() == null ? window : getParentView().getParentWindow(level + 1);
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }
}
