package com.tsoft.config;

public class SectionParam {
    private String name;
    private Object defaultValue;

    public SectionParam(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
