package com.tsoft.dict;

import com.tsoft.mvc.Form;
import com.tsoft.dict.dict.DictView;
import com.tsoft.dict.settings.SettingsView;
import com.tsoft.dict.wordtrainer.WordTrainerView;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

public class AppForm extends Form {
    @Override
    public void createComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        ImageIcon trainerIcon = new ImageIcon(getView().getImage("TrayIcon.gif"));
        JComponent trainerPanel = getView(WordTrainerView.class).createMainForm();
        tabbedPane.addTab("Word Trainer", trainerIcon, trainerPanel, "Word Trainer");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        ImageIcon dictIcon = new ImageIcon(getView().getImage("TrayIcon.gif"));
        JComponent dictPanel = getView(DictView.class).createMainForm();
        tabbedPane.addTab("Dictionary", dictIcon, dictPanel, "Dictionary");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        ImageIcon settingsIcon = new ImageIcon(getView().getImage("TrayIcon.gif"));
        JComponent settingsPanel = getView(SettingsView.class).createMainForm();
        tabbedPane.addTab("Settings", settingsIcon, settingsPanel, "App Settings");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        add(tabbedPane);
    }
}
