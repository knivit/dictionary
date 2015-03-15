package com.tsoft.dict;

import com.tsoft.mvc.Form;
import com.tsoft.mvc.View;
import com.tsoft.ui.binder.EventListener;
import com.tsoft.config.ConfigSection;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class AppView extends View {
    private Logger logger = Logger.getLogger(AppView.class.getName());

    public static String WINDOW_CAPTION = "Language Trainer";

    private PopupMenu popup;

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > exitClick")
    private MenuItem item = new MenuItem("Exit");

    @Override
    public void prepareWindow() {
        getWindow().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                minimizeToTray();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                minimizeToTray();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // don't fire an event when the frame was minimized to tray
                if (getWindow().isVisible()) {
                    getController().viewClosed();
                }
            }
        });

        addIconToTray();
    }

    private void minimizeToTray() {
        ((JFrame)getWindow()).setVisible(false);
    }

    private enum TrayIconAction {
        Click, DoubleClick
    }

    private boolean addIconToTray() {
        if (!SystemTray.isSupported()) {
            return false;
        }

        PopupMenu popupMenu = getTrayPopupMenu();
        if (popupMenu == null) {
            return false;
        }

        Image trayImage = getImage("TrayIcon.gif");
        if (trayImage == null) {
            return false;
        }

        JFrame frame = (JFrame)getWindow();
        TrayIcon trayIcon = new TrayIcon(trayImage, frame.getTitle(), popupMenu);

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doTrayIconAction(TrayIconAction.DoubleClick);
            }
        });

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    doTrayIconAction(TrayIconAction.Click);
                }
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
            return true;
        } catch (AWTException ex) {
            logger.log(Level.SEVERE, "Unavailable to minimize this window to tray", ex);
        }

        return false;
    }

    private void doTrayIconAction(TrayIconAction action) {
        ConfigSection section = getController().getConfig().getSection(AppConfig.SETTINGS_SECTION);
        boolean isOneTrayClick = section.getBoolean(AppConfig.SETTINGS_ONE_TRAY_CLICK);
        TrayIconAction neededAction = isOneTrayClick ? TrayIconAction.Click : TrayIconAction.DoubleClick;

        if (action != neededAction) {
            return;
        }

        final JFrame frame = (JFrame)getWindow();
        if (frame.isVisible()) {
            getWindow().setVisible(false);
        } else {
            frame.setVisible(true);
            frame.setState(JFrame.NORMAL);
        }
    }

    @Override
    public AppController getController() {
        return (AppController)controller;
    }

    private PopupMenu getTrayPopupMenu() {
        if (popup == null) {
            popup = new PopupMenu();
            popup.add(item);
        }
        
        return popup;
    }

    public void exitClick(ActionEvent e) {
        controller.viewClosed();
        System.exit(0);
    }

    @Override
    public Class<? extends Form> getMainFormClass() {
        return AppForm.class;
    }

    @Override
    public String getCaption() {
        return WINDOW_CAPTION;
    }
}
