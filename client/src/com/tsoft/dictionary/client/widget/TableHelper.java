package com.tsoft.dictionary.client.widget;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class TableHelper {
    private TableHelper() { }

    public static void addWidget(CellPanel table, Widget widget) {
        addWidget(table, widget, "100%");
    }

    public static void addWidget(CellPanel table, Widget widget, int size) {
        addWidget(table, widget, Integer.toString(size) + "px");
    }

    public static void addWidget(CellPanel table, Widget widget, String size) {
        if (table instanceof HorizontalPanel) {
            addHorizontalWidget(table, widget, size);
        } else {
            addVerticalWidget(table, widget, size);
        }
    }

    private static void addHorizontalWidget(CellPanel table, Widget widget, String width) {
        StyleHelper.setWidth(widget, width);
        table.add(widget);
        table.setCellWidth(widget, width);
    }

    private static void addVerticalWidget(CellPanel table, Widget widget, String height) {
        StyleHelper.setHeight(widget, height);
        table.add(widget);
        table.setCellHeight(widget, height);
    }

    public static void addSeparator(CellPanel table) {
        addSeparator(table, "100%");
    }

    public static void addSeparator(CellPanel table, int size) {
        addSeparator(table, Integer.toString(size) + "px");
    }

    public static void addSeparator(CellPanel table, String size) {
        if (table instanceof HorizontalPanel) {
            addHorizontalSeparator(table, size);
        } else {
            addVerticalSeparator(table, size);
        }
    }

    private static void addHorizontalSeparator(CellPanel table, String width) {
        HorizontalSeparator separator = new HorizontalSeparator(width);
        table.add(separator);
        table.setCellWidth(separator, width);
    }

    private static void addVerticalSeparator(CellPanel table, String height) {
        VerticalSeparator separator = new VerticalSeparator(height);
        table.add(separator);
        table.setCellHeight(separator, height);
    }
}
