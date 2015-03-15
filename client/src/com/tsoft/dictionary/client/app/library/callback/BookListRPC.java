package com.tsoft.dictionary.client.app.library.callback;

import com.tsoft.dictionary.server.app.web.library.BookListResponseTO;
import com.tsoft.dictionary.server.app.web.library.BooksFilterRequestTO;

public interface BookListRPC {
    public void beforeBookListRPC(BooksFilterRequestTO requestTO);

    public void afterBookListRPC(BookListResponseTO responseTO, boolean success);
}
