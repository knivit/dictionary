package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.library.BookListResponseTO;
import com.tsoft.dictionary.server.app.web.library.BookIdRequestTO;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO;
import com.tsoft.dictionary.server.app.web.library.BooksFilterRequestTO;
import com.tsoft.dictionary.server.app.web.library.BookInfoResponseTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.server.app.web.library.LibraryServletInterfaceAsync;

public class LibraryServletProxy extends ServerServletProxy {
    private LibraryServletInterfaceAsync libraryServletAsync;

    public LibraryServletProxy(ServerServlets serverServlets, LibraryServletInterfaceAsync libraryServletAsync) {
        super(serverServlets);
        this.libraryServletAsync = libraryServletAsync;
    }

    public void getSearchParameters(AsyncCallback<BookParametersResponseTO> callback) {
       libraryServletAsync.getSearchParameters(getServerServlets().getUserInfo(), callback);
    }

    public void getBookList(BooksFilterRequestTO requestTO, AsyncCallback<BookListResponseTO> callback) {
        libraryServletAsync.getBookList(getServerServlets().getUserInfo(), requestTO, callback);
    }

    public void getBookInfo(BookIdRequestTO requestTO, AsyncCallback<BookInfoResponseTO> callback) {
        libraryServletAsync.getBookInfo(getServerServlets().getUserInfo(), requestTO, callback);
    }
}
