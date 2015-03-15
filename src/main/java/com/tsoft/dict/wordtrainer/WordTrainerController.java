package com.tsoft.dict.wordtrainer;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Controller;
import com.tsoft.mvc.MVC;
import com.tsoft.config.ConfigController;
import com.tsoft.config.ConfigModel;
import com.tsoft.config.ConfigView;
import com.tsoft.dict.base.BaseWord;
import com.tsoft.dict.base.Stat;

public class WordTrainerController extends Controller {
    @Override
    public void init() throws Exception {
        addSingleton(AppConfig.class, ConfigModel.class, ConfigView.class);
    }

    public BaseWord getRandomWord() throws Exception {
        return getModel().getRandomWord();
    }

    public Stat getStat(BaseWord word) throws Exception {
        return getModel().getStat(word);
    }

    @Override
    public WordTrainerModel getModel() {
        return (WordTrainerModel)model;
    }

    public ConfigController getConfig() {
        return (ConfigController)getSingleton(AppConfig.class).getController();
    }

    public static void main(String[] args) throws Exception {
        MVC mvc = MVC.newInstance(WordTrainerController.class, WordTrainerModel.class, WordTrainerView.class);
        mvc.run(false);
    }
}
