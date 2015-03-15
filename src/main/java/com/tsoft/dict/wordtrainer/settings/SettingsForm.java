package com.tsoft.dict.wordtrainer.settings;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Form;
import com.tsoft.ui.binder.EventListener;
import com.tsoft.ui.layout.MigLayoutHelper;
import com.tsoft.config.ConfigSection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class SettingsForm extends Form {
    // path to the dictionaries folder
    private JTextField dictPathText = new JTextField(40);

    // quantity of the translated variants for the current word (0 - don't show at all)
    private JTextField wordsOnPage = new JTextField(2);

    // direction of translation
    private JCheckBox reverseDirection = new JCheckBox("Reverse the Direction of the Translation");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > dictPathClick")
    private JButton dictPathButton = new JButton("...");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > saveClick")
    private JButton saveButton = new JButton("Save and Close");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > closeClick")
    private JButton closeButton = new JButton("Close");

    @Override
    public void createComponents() {
        setLayout(new MigLayout());

        add(new JLabel("Path to the Dictionaries files"), "wrap");
        add(dictPathText, "growx");
        add(dictPathButton, "wrap");
        MigLayoutHelper.createSeparator(this, "Settings");
        add(new JLabel("Words on the Page (0 - don't show)"), "split 2");
        add(wordsOnPage, "wrap");
        add(reverseDirection, "wrap");
        add(saveButton, "align right");
        add(closeButton, "wrap");

        ConfigSection section = getController().getConfig().getSection(AppConfig.WORD_TRAINER_SECTION);
        dictPathText.setText(section.getString(AppConfig.WORD_TRAINER_DICTIONARY_DIR));
        wordsOnPage.setText(section.getString(AppConfig.WORD_TRAINER_WORDS_ON_PAGE));
        reverseDirection.setSelected(section.getBoolean(AppConfig.WORD_TRAINER_REVERSE_DICTIONARY));
    }

    public SettingsController getController() {
        return (SettingsController)getView().getController();
    }

    public void dictPathClick(ActionEvent e) {
        JFileChooser pathChooser = new JFileChooser(dictPathText.getText());
        pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (pathChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        dictPathText.setText(pathChooser.getSelectedFile().getAbsolutePath());
    }

    public void saveClick(ActionEvent e) {
        ConfigSection section = getController().getConfig().getSection(AppConfig.WORD_TRAINER_SECTION);
        section.setBoolean(AppConfig.WORD_TRAINER_REVERSE_DICTIONARY, reverseDirection.isSelected());
        section.setString(AppConfig.WORD_TRAINER_DICTIONARY_DIR, dictPathText.getText());
        section.setInt(AppConfig.WORD_TRAINER_WORDS_ON_PAGE, Integer.parseInt(wordsOnPage.getText()));

        close();
    }

    public void closeClick(ActionEvent e) {
        close();
    }
}
