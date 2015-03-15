package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("library")
public interface LibraryServletInterface extends RemoteService {
    public BookParametersResponseTO getSearchParameters(UserInfo userInfo);

    public LibraryInfoResponseTO getLibraryInfo(UserInfo userInfo);

    public BookListResponseTO getBookList(UserInfo userInfo, BooksFilterRequestTO requestTO);

    public BookInfoResponseTO getBookInfo(UserInfo userInfo, BookIdRequestTO requestTO);
}
