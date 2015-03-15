package com.tsoft.dict.dict;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Controller;
import com.tsoft.mvc.MVC;
import com.tsoft.dict.base.BaseRecord;
import com.tsoft.config.ConfigController;
import com.tsoft.config.ConfigModel;
import com.tsoft.config.ConfigView;

public class DictController extends Controller {
    @Override
    public void init() throws Exception {
        addSingleton(AppConfig.class, ConfigModel.class, ConfigView.class);
    }

    public BaseRecord getRecord(String word) {
        return getModel().getRecord(word);
    }

    @Override
    public DictModel getModel() {
        return (DictModel)model;
    }

    public ConfigController getConfig() {
        return (ConfigController)getSingleton(AppConfig.class).getController();
    }

    public static void main(String[] args) throws Exception {
        MVC mvc = MVC.newInstance(DictController.class, DictModel.class, DictView.class);
        mvc.run(false);
    }
}
