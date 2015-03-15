package com.tsoft.util;

public class NameValue {
    private String name;
    private String value;

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public NameValue(NameValue value) {
        this.name = value.getName();
        this.value = value.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Name='" + getName() + "', Value='" + getValue() + "'";
    }
}
