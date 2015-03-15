package com.tsoft.dict.wordtrainer;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.MVC;
import com.tsoft.dict.wordtrainer.settings.SettingsController;
import com.tsoft.dict.wordtrainer.settings.SettingsModel;
import com.tsoft.mvc.Form;
import com.tsoft.ui.binder.EventListener;
import com.tsoft.dict.wordtrainer.settings.SettingsView;
import com.tsoft.ui.layout.MigLayoutHelper;
import com.tsoft.config.ConfigSection;
import com.tsoft.dict.base.BaseWord;
import com.tsoft.dict.base.Stat;
import com.tsoft.util.file.DirScanner;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class WordTrainerForm extends Form {
    private static Logger logger = Logger.getLogger(WordTrainerForm.class.getName());

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > selectDictionaryAction")
    private JComboBox dictionaries = new JComboBox();

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > refreshDictionaryListClick")
    private JButton refreshDictionaries = new JButton("Refresh List");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > checkClick")
    private JButton OK = new JButton("Check");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > checkClick")
    private JTextField userInput = new JTextField(30);

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > tipClick")
    private JButton tip = new JButton("Tip");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > nextClick")
    private JButton next = new JButton("Next");

    @EventListener(type = ActionListener.class, mappings = "actionPerformed > settingsClick")
    private JButton settings = new JButton("Settings");

    private BaseWord word;
    private JLabel translateLabel;

    private JPanel hintPanel = new JPanel();
    private JLabel[] hintLabels;

    @Override
    public void createComponents() {
        setLayout(new MigLayout("", "[grow,fill][][][][grow,fill]", "[][c,pref!][][c,grow][][c,pref!][c,grow,fill]"));

        MigLayoutHelper.createSeparator(this, "Dictionary");
        add(dictionaries, "span 4");
        add(refreshDictionaries, "wrap");

        MigLayoutHelper.createSeparator(this, "Word for translation");
        translateLabel = new JLabel();
        Font translateFont = new Font(null, Font.BOLD, 14);
        translateLabel.setFont(translateFont);
        translateLabel.setForeground(Color.RED);
        add(translateLabel, "span");

        MigLayoutHelper.createSeparator(this, "Type your translation here");
        add(userInput);
        add(OK);
        add(tip);
        add(next);
        add(settings, "wrap");

        hintPanel.setLayout(new MigLayout("", "[grow,fill]", ""));
        add(hintPanel, "span");

        resetBase();

        refreshDictionaryList();
        refreshForm();
        refreshWord();
    }

    private WordTrainerController getController() {
        return (WordTrainerController)getView().getController();
    }

    private Stat getStat(BaseWord word) {
        try {
            return getController().getStat(word);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Can't read a stat for the word");
            return null;
        }
    }

    private BaseWord getRandomWord() {
        BaseWord newWord;
        try {
            newWord = getController().getRandomWord();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Can't read a word from dictionary");
            return null;
        }

        newWord = reverseRecord(newWord);
        return newWord;
    }

    private BaseWord reverseRecord(BaseWord record) {
        BaseWord newWord = new BaseWord(record);

        ConfigSection section = getConfigSection();
        boolean isReverse = section.getBoolean(AppConfig.WORD_TRAINER_REVERSE_DICTIONARY);
        if (isReverse) {
            String temp = newWord.getWord();
            newWord.setWord(newWord.getValue());
            newWord.setValue(temp);
        }
        return newWord;
    }

    private void refreshWord() {
        // clear input
        userInput.setText("");

        // source word
        word = getRandomWord();
        translateLabel.setText(word.getWord());
        Stat stat = getStat(word);
        stat.incShowCount();

        if (getHintCount() == 0) {
            return;
        }

        // translated variants
        int correctPos = (int)(Math.random() * hintLabels.length);
        for (int i = 0; i < hintLabels.length; i ++) {
            String variantTranslation;
            if (i == correctPos) {
                variantTranslation = word.getValue();
            } else {
                BaseWord variant = getRandomWord();
                variantTranslation = variant.getValue();
            }

            hintLabels[i].setText(variantTranslation);
        }

        validate();
    }

    private ConfigSection getConfigSection() {
        return getController().getConfig().getSection(AppConfig.WORD_TRAINER_SECTION);
    }

    public void checkClick(ActionEvent e) {
        if (word.getValue().equalsIgnoreCase(userInput.getText())) {
            JOptionPane.showMessageDialog(null, "Yes");
            refreshWord();
        } else {
            JOptionPane.showMessageDialog(null, "No, try again");
            userInput.setText("");
        }
    }

    public void nextClick(ActionEvent e) {
        refreshWord();
    }

    public void tipClick(ActionEvent e) {
        Stat stat = getStat(word);
        stat.incTipCount();

        JOptionPane.showMessageDialog(getView().getWindow(), word.getValue());
        userInput.setText("");
    }

    public void selectDictionaryAction(ActionEvent e) {
        ConfigSection section = getConfigSection();
        String fileName = (String)dictionaries.getSelectedItem();
        section.setString(AppConfig.WORD_TRAINER_FILE, fileName);

        resetBase();

        refreshForm();
        refreshWord();
    }

    private void resetBase() {
        getController().getModel().close();
    }

    public void refreshDictionaryListClick(ActionEvent e) {
        refreshDictionaryList();
    }

    private void refreshDictionaryList() {
        ConfigSection section = getConfigSection();
        String dir = section.getString(AppConfig.WORD_TRAINER_DICTIONARY_DIR);
        List<String> fileList = DirScanner.getFileNames(new File(dir), "*.txt");
        String[] files = fileList.toArray(new String[0]);
        dictionaries.setModel(new DefaultComboBoxModel(files));

        String selectedFile = section.getString(AppConfig.WORD_TRAINER_FILE);

        int fileIndex = fileList.indexOf(selectedFile);
        if (fileIndex == -1) {
            if (files.length == 0) {
                return;
            }

            // select first field by default
            fileIndex = 0;
            section.setString(AppConfig.WORD_TRAINER_FILE, files[fileIndex]);
        }

        dictionaries.setSelectedIndex(fileIndex);
    }

    private boolean isDictionarySelected() {
        return dictionaries.getSelectedIndex() != -1;
    }

    private void clearHintPanel() {
        hintPanel.removeAll();
        hintPanel.validate();
    }

    private int getHintCount() {
        if (!isDictionarySelected()) {
            return 0;
        }

        ConfigSection section = getConfigSection();
        return section.getInt(AppConfig.WORD_TRAINER_WORDS_ON_PAGE);
    }

    private void refreshForm() {
        clearHintPanel();
        if (getHintCount() == 0) {
            return;
        }

        hintLabels = new JLabel[getHintCount()];
        MigLayoutHelper.createSeparator(hintPanel, "Hints");
        for (int i = 0; i < hintLabels.length; i ++) {
            JLabel variantLabel = new JLabel();
            Font variantFont = new Font(null, Font.BOLD, 14);
            variantLabel.setFont(variantFont);
            variantLabel.setForeground(Color.BLUE);
            hintLabels[i] = variantLabel;
            hintPanel.add(variantLabel, "wrap");
        }
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
