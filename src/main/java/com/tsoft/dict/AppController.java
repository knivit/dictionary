package com.tsoft.dict;

import com.tsoft.dict.wordtrainer.WordTrainerController;
import com.tsoft.dict.settings.SettingsController;
import com.tsoft.dict.dict.DictController;
import com.tsoft.mvc.Controller;
import com.tsoft.mvc.MVC;
import com.tsoft.dict.dict.DictModel;
import com.tsoft.dict.settings.SettingsModel;
import com.tsoft.dict.wordtrainer.WordTrainerModel;
import com.tsoft.dict.dict.DictView;
import com.tsoft.dict.settings.SettingsView;
import com.tsoft.dict.wordtrainer.WordTrainerView;
import com.tsoft.config.ConfigController;
import com.tsoft.config.ConfigModel;
import com.tsoft.config.ConfigView;
import java.util.logging.Logger;

public class AppController extends Controller {
    private Logger logger = Logger.getLogger(AppController.class.getName());

    @Override
    public void init() throws Exception {
        addSingleton(AppConfig.class, ConfigModel.class, ConfigView.class);
        
        addMember(WordTrainerController.class, WordTrainerModel.class, WordTrainerView.class);
        addMember(DictController.class, DictModel.class, DictView.class);
        addMember(SettingsController.class, SettingsModel.class, SettingsView.class);
    }
    
    @Override
    public AppModel getModel() {
        return (AppModel)model;
    }

    public ConfigController getConfig() {
        return (ConfigController)getSingleton(AppConfig.class).getController();
    }

    // self-teaching mode:
    // 1) show 10 words, wait 10 right answers
    // 2) again show this words but without translation
    // 3) user should input a corect translation

    // autopopup from system tray periodically
    // 1) user set up a period (min)
    // 2) after the app will be minimized to tray the timer starts
    // 3) when the timer stops then app maximized from the tray

    // a phrase writing training - Sentence Builder
    // shown: Bob <?> to school
    // user should to write: Bob going to school
    // all combination from verbs tense + I, he, she, it, they etc

    // translate direction in dictionary
    public static void main(String[] args) throws Exception {
        MVC mvc = MVC.newInstance(AppController.class, AppModel.class, AppView.class);
        mvc.run(false);
    }
}
