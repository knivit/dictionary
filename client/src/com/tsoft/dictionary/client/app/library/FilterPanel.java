package com.tsoft.dictionary.client.app.library;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.CssStyles;
import com.tsoft.dictionary.client.app.library.callback.BookListRPC;
import com.tsoft.dictionary.client.app.library.callback.SearchParametersRPC;
import com.tsoft.dictionary.client.app.library.filter.WidgetParameter;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.client.widget.TableHelper;
import com.tsoft.dictionary.client.widget.handler.EnterKeyPressHandler;
import com.tsoft.dictionary.server.app.web.library.BookListResponseTO;
import com.tsoft.dictionary.server.app.web.library.BooksFilterRequestTO;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.Parameter;
import java.util.ArrayList;

public class FilterPanel extends AbstractPanel<LibraryForm> implements SearchParametersRPC, BookListRPC {
    interface Binder extends UiBinder<Widget, FilterPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    private ArrayList<WidgetParameter> widgetParameters = new ArrayList<WidgetParameter>();

    @UiField HorizontalPanel filterElements;
    @UiField Button findButton;

    public FilterPanel() {
        super(binder);
    }

    @UiHandler("findButton")
    public void findButtonClick(ClickEvent event) {
        getForm().populateBookList();
    }

    @Override
    public void beforeSearchParameterListRPC(Void requestTO) { }

    private void createFilterElements(ArrayList<Parameter> parameters) {
        clearFilterElements();

        boolean first = true;
        for (Parameter parameter : parameters) {
            VerticalPanel elementPanel = new VerticalPanel();
            HTML caption = new HTML(parameter.getCaption());
            caption.addStyleDependentName(CssStyles.HTML_CAPTION);
            elementPanel.add(caption);
            TableHelper.addSeparator(elementPanel, 5);

            WidgetParameter widgetParameter = WidgetParameter.newInstance(parameter);
            widgetParameters.add(widgetParameter);

            Widget widget = widgetParameter.getWidget();
            elementPanel.add(widget);
            if (!first) {
                TableHelper.addSeparator(filterElements, 8);
            }
            
            if (widget instanceof HasKeyPressHandlers) {
                HasKeyPressHandlers keyPressWidget = (HasKeyPressHandlers)widget;
                keyPressWidget.addKeyPressHandler(new EnterKeyPressHandler(findButton));
            }
            filterElements.add(elementPanel);

            first = false;
        }
        TableHelper.addSeparator(filterElements);

        // add Find button
        VerticalPanel elementPanel = new VerticalPanel();
        HTML caption = new HTML(" ");
        caption.addStyleDependentName(CssStyles.HTML_CAPTION);
        elementPanel.add(caption);
        TableHelper.addSeparator(elementPanel, 5);
        elementPanel.add(findButton);
        filterElements.add(elementPanel);
    }

    private void clearFilterElements() {
        filterElements.clear();
        widgetParameters.clear();
    }

    @Override
    public void afterSearchParameterListRPC(BookParametersResponseTO responseTO, boolean success) {
        if (success && responseTO.getParameters() != null) {
            createFilterElements(responseTO.getParameters());
        } else {
            clearFilterElements();
        }
    }

    @Override
    public void beforeBookListRPC(BooksFilterRequestTO requestTO) {
        for (WidgetParameter widgetParameter : widgetParameters) {
            requestTO.add(widgetParameter.getParameterName(), widgetParameter.getValue());
        }
    }

    @Override
    public void afterBookListRPC(BookListResponseTO responseTO, boolean success) { }
}
