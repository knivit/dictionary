package com.tsoft.config;

import com.tsoft.mvc.Controller;
import java.io.File;
import java.io.Reader;

public class ConfigController extends Controller {
    private String fileName;
    private Reader reader;

    public ConfigController(String fileName) {
        this.fileName = System.getProperty("user.home") + File.separator + fileName;
    }

    public ConfigController(Reader reader) {
        this.reader = reader;
    }

    @Override
    public void init() throws Exception {
        getModel().setFileName(fileName);
        getModel().setReader(reader);
    }

    public ConfigSection getSection(String name) {
        return getModel().getSection(name);
    }

    public ConfigSection findSection(String name) {
        return getModel().findSection(name);
    }

    @Override
    public ConfigModel getModel() {
        return (ConfigModel)super.getModel();
    }

    @Override
    public void viewClosed() {
        super.viewClosed();
        getModel().close();
    }
}
