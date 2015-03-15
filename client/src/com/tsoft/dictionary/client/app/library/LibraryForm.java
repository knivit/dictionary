package com.tsoft.dictionary.client.app.library;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.client.server.app.LibraryServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.server.app.web.library.BookListResponseTO;
import com.tsoft.dictionary.server.app.web.library.BooksFilterRequestTO;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO;

public class LibraryForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, LibraryForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField FilterPanel filterPanel;
    @UiField BookListPanel bookListPanel;

    public LibraryForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() {
        filterPanel.setForm(this);
        bookListPanel.setForm(this);

        populateSearchParameters();
    }

    private LibraryServletProxy getLibraryServlet() {
        return getServerServlets().getLibraryServlet();
    }

    private class SearchParametersCallback extends DefaultAsyncCallback<Void, BookParametersResponseTO> {
        public SearchParametersCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            filterPanel.beforeSearchParameterListRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(BookParametersResponseTO responseTO, boolean isSuccess) {
            filterPanel.afterSearchParameterListRPC(responseTO, isSuccess);
        }
    }

    public void populateSearchParameters() {
        getLibraryServlet().getSearchParameters(new SearchParametersCallback(null));
    }

    private class BookListCallback extends DefaultAsyncCallback<BooksFilterRequestTO, BookListResponseTO> {
        public BookListCallback(BooksFilterRequestTO requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(BooksFilterRequestTO requestTO) {
            filterPanel.beforeBookListRPC(requestTO);
            bookListPanel.beforeBookListRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(BookListResponseTO responseTO, boolean isSuccess) {
            filterPanel.afterBookListRPC(responseTO, isSuccess);
            bookListPanel.afterBookListRPC(responseTO, isSuccess);
        }
    }

    public void populateBookList() {
        BooksFilterRequestTO requestTO = new BooksFilterRequestTO();
        getLibraryServlet().getBookList(requestTO, new BookListCallback(requestTO));
    }
}
