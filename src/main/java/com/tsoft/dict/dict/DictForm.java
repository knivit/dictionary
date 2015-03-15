package com.tsoft.dict.dict;

import com.tsoft.dict.base.BaseRecord;
import com.tsoft.mvc.Form;
import com.tsoft.mvc.MVC;
import com.tsoft.dict.dict.settings.SettingsController;
import com.tsoft.dict.dict.settings.SettingsModel;
import com.tsoft.dict.dict.settings.SettingsView;
import com.tsoft.ui.binder.EventListener;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class DictForm extends Form {
    private static Logger logger = Logger.getLogger(DictForm.class.getName());

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > translateClick")
    private JButton translate = new JButton("Translate");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > translateClick")
    private JTextField userInput = new JTextField(30);

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > settingsClick")
    private JButton settings = new JButton("Settings");

    JTextArea translateArea = new JTextArea(8, 30);

    @Override
    public void createComponents() {
        setLayout(new MigLayout("", "[grow,fill][][]", "[][c,pref!][c,grow,fill]"));

        add(new JLabel("Word"), "wrap");
        add(userInput);
        add(translate);
        add(settings, "wrap");

        Font variantFont = new Font(null, Font.BOLD, 14);
        translateArea.setFont(variantFont);
        translateArea.setEditable(false);
        JScrollPane translatePane = new JScrollPane(translateArea);
        add(translatePane, "span");
    }

    private DictController getController() {
        return (DictController)getView().getController();
    }

    public void translateClick(ActionEvent e) {
        String word = userInput.getText();
        BaseRecord record = null;
        if (word != null) {
            record = getController().getRecord(word);
        }

        translateArea.setText(record.getValue());
        translateArea.setCaretPosition(0);
        
        userInput.selectAll();
        userInput.requestFocusInWindow();
    }

    public void settingsClick(ActionEvent e) {
        try {
            MVC mvc = MVC.newInstance(SettingsController.class, SettingsModel.class, SettingsView.class);
            mvc.run(getView().getController(), true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "An exception during execution occured", ex);
        }
    }
}
