package com.tsoft.dictionary.server.app.web.dictionary.base;

public class DictionaryFile {
    private String name;
    private String fileName;

    public DictionaryFile(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getName() {
        return name;
    }
}
