package com.tsoft.dictionary.server.app.service.library;

import com.google.appengine.api.datastore.Key;
import com.tsoft.dictionary.server.app.UserInfo;
import java.util.List;

public interface LibraryService {
    public BookPO findBookPO(Key id);

    public OpenBookPO findOpenBookPO(Key id);

    public LibraryInfoTO getLibraryInfo(UserInfo userInfo);

    public List<BookTO> findBooks(String searchList, String searchValue, String yearFrom, String yearTo, String genre);
}
