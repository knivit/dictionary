package com.tsoft.dict.settings;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Form;
import com.tsoft.ui.binder.EventListener;
import com.tsoft.config.ConfigSection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class SettingsForm extends Form {
    private JCheckBox oneTrayClick = new JCheckBox("One mouse click to restore from the tray (by default it need double click)");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > saveClick")
    private JButton saveButton = new JButton("Save");

    @Override
    public void createComponents() {
        JPanel buttonPanel = new JPanel();
        MigLayout buttonLayout = new MigLayout("", "push[]");
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.add(saveButton);

        MigLayout layout = new MigLayout("wrap", "[fill]");
        setLayout(layout);
        add(oneTrayClick);
        add(buttonPanel);

        ConfigSection section = getController().getConfig().getSection(AppConfig.SETTINGS_SECTION);
        oneTrayClick.setSelected(section.getBoolean(AppConfig.SETTINGS_ONE_TRAY_CLICK));
    }

    public SettingsController getController() {
        return (SettingsController)getView().getController();
    }

    public void saveClick(ActionEvent e) {
        ConfigSection section = getController().getConfig().getSection(AppConfig.SETTINGS_SECTION);
        section.setBoolean(AppConfig.SETTINGS_ONE_TRAY_CLICK, oneTrayClick.isSelected());
    }
}
