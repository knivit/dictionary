package com.tsoft.dict.settings;

import com.tsoft.mvc.Controller;
import com.tsoft.mvc.MVC;
import com.tsoft.dict.AppConfig;
import com.tsoft.config.ConfigController;
import com.tsoft.config.ConfigModel;
import com.tsoft.config.ConfigView;

public class SettingsController extends Controller {
    @Override
    public void init() throws Exception {
        addSingleton(AppConfig.class, ConfigModel.class, ConfigView.class);
    }

    public ConfigController getConfig() {
        return (ConfigController)getSingleton(AppConfig.class).getController();
    }

    public static void main(String[] args) throws Exception {
        MVC mvc = MVC.newInstance(SettingsController.class, SettingsModel.class, SettingsView.class);
        mvc.run(false);
    }
}
