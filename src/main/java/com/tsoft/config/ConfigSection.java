package com.tsoft.config;

import com.tsoft.util.NameValue;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigSection {
    private String name;
    private ArrayList<NameValue> propertyList = new ArrayList<NameValue>();
    private boolean isChanged;

    public ConfigSection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public boolean getBoolean(SectionParam param) {
        NameValue property = findProperty(param.getName());
        if (property == null) {
            boolean defaultValue = (Boolean)param.getDefaultValue();
            setBoolean(param, defaultValue);
            return defaultValue;
        }

        return new Boolean(property.getValue());
    }

    public void setBoolean(SectionParam param, boolean value) {
        setString(param, Boolean.toString(value));
    }

    public int getInt(SectionParam param) {
        NameValue property = findProperty(param.getName());
        if (property == null) {
            int defaultValue = (Integer)param.getDefaultValue();
            setInt(param, defaultValue);
            return defaultValue;
        }

        return new Integer(property.getValue());
   }

    public void setInt(SectionParam param, int value) {
        setString(param, Integer.toString(value));
    }

    public String getString(SectionParam param) {
        NameValue property = findProperty(param.getName());
        if (property == null) {
            String defaultValue = (String)param.getDefaultValue();
            setString(param, defaultValue);
            return defaultValue;
        }

        return property.getValue();

    }

    public void setString(SectionParam param, String value) {
        NameValue property = findProperty(param.getName());
        if (property == null) {
            property = new NameValue(param.getName(), value);
            propertyList.add(property);
            isChanged = true;
            return;
        }

        if (value == null) {
            if (property.getValue() == null) {
                return;
            }

            property.setValue(null);
            isChanged = true;
            return;
        }

        if (value.equals(property.getValue())) {
            return;
        }

        property.setValue(value);
        isChanged = true;
    }

    public void addProperty(String line) {
        String name, value;
        int n = line.indexOf('=');
        if (n == -1) {
            name = line;
            value = null;
        } else {
            name = line.substring(0, n).trim();
            value = line.substring(n + 1);
        }

        NameValue property = findProperty(name);
        if (property == null) {
            property = new NameValue(name, value);
            propertyList.add(property);
        } else {
            property.setValue(value);
        }
    }

    private NameValue findProperty(String name) {
        for (NameValue property : propertyList) {
            if (property.getName().equalsIgnoreCase(name)) {
                return property;
            }
        }

        return null;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void save(BufferedWriter writer) throws IOException {
        for (NameValue property : propertyList) {
            if (property.getValue() == null) {
                writer.write(property.getName() + "=\n");
            } else {
                writer.write(property.getName() + "=" + property.getValue() + "\n");
            }
        }
    }
}
