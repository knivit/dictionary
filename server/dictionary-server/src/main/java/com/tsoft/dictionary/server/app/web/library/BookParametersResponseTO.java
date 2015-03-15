package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class BookParametersResponseTO implements IsSerializable {
    public static enum ParameterType {
        STRING, LIST
    }

    public abstract static class Parameter implements Serializable {
        private String name;
        private String caption;

        public abstract ParameterType getType();

        public Parameter() { }

        public Parameter(String name, String caption) {
            this.name = name;
            this.caption = caption;
        }

        public String getName() {
            return name;
        }

        public String getCaption() {
            return caption;
        }
    }

    public static class StringParameter extends Parameter {
        public StringParameter() { }

        public StringParameter(String name, String caption) {
            super(name, caption);
        }

        @Override
        public ParameterType getType() {
            return ParameterType.STRING;
        }
    }

    public static class ListParameter extends Parameter {
        private ArrayList<String> values = new ArrayList<String>();
        private boolean isSelectOnly;
        private int selectedIndex;

        public ListParameter() { }

        public ListParameter(String name, String caption) {
            super(name, caption);
        }

        @Override
        public ParameterType getType() {
            return ParameterType.LIST;
        }

        public void add(Collection<String> values) {
            this.values.addAll(values);
        }

        public void add(String value) {
            values.add(value);
        }

        public ArrayList<String> getValues() {
            return values;
        }

        public boolean isIsSelectOnly() {
            return isSelectOnly;
        }

        public void setIsSelectOnly(boolean isSelectOnly) {
            this.isSelectOnly = isSelectOnly;
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }
    }

    private ArrayList<Parameter> parameters = new ArrayList<Parameter>();

    public BookParametersResponseTO() { }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void add(Collection<Parameter> parameters) {
        this.parameters.addAll(parameters);
    }

    public void add(Parameter parameter) {
        parameters.add(parameter);
    }
}
