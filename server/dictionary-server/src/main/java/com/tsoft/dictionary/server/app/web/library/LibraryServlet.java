package com.tsoft.dictionary.server.app.web.library;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.service.library.BookTO;
import com.tsoft.dictionary.server.app.service.library.LibraryInfoTO;
import com.tsoft.dictionary.server.app.service.library.LibraryService;
import com.tsoft.dictionary.server.app.service.library.OpenBookTO;
import com.tsoft.dictionary.server.app.web.ServerServlet;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.ListParameter;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.StringParameter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class LibraryServlet extends ServerServlet implements LibraryServletInterface {
    private static final Logger logger = Logger.getLogger(LibraryServlet.class.getName());

    // not for localize
    public static final String SEARCH_PARAM_NAME = "ID_S";
    public static final String SEARCH_PARAM_VALUE = "ID_V";
    public static final String SEARCH_PARAM_TITLE = "Title";
    public static final String SEARCH_PARAM_AUTHOR = "Author";
    public static final String SEARCH_PARAM_CONTENT = "Content";
    public static final String YEAR_FROM_PARAM = "ID_Y1";
    public static final String YEAR_TO_PARAM = "ID_Y2";
    public static final String GENRE_PARAM = "ID_G";

    public static final String GENRE_PARAM_ALL = "All";
    public static final String[] GENRE_PARAM_VALUES = {"Fantasy", "Science Fiction"};

    @Override
    public BookParametersResponseTO getSearchParameters(UserInfo userInfo) {
        BookParametersResponseTO responseTO = new BookParametersResponseTO();

        ListParameter searchList = new ListParameter(SEARCH_PARAM_NAME, "Parameter");
        searchList.add("");
        searchList.add(SEARCH_PARAM_TITLE);
        searchList.add(SEARCH_PARAM_AUTHOR);
        searchList.add(SEARCH_PARAM_CONTENT);
        responseTO.add(searchList);

        StringParameter searchValue = new StringParameter(SEARCH_PARAM_VALUE, "Parameter Value");
        responseTO.add(searchValue);

        StringParameter year1 = new StringParameter(YEAR_FROM_PARAM, "From Year");
        responseTO.add(year1);

        StringParameter year2 = new StringParameter(YEAR_TO_PARAM, "To Year");
        responseTO.add(year2);

        ListParameter genreList = new ListParameter(GENRE_PARAM, "Genre");
        genreList.add(GENRE_PARAM_ALL);
        for (String genre : GENRE_PARAM_VALUES) {
          genreList.add(genre);
        }
        responseTO.add(genreList);

        return responseTO;
    }

    @Override
    public LibraryInfoResponseTO getLibraryInfo(UserInfo userInfo) {
        LibraryService libraryService = lookup(LibraryService.class);
        LibraryInfoTO libraryInfoTO = libraryService.getLibraryInfo(userInfo);

        LibraryInfoResponseTO responseTO = populateLibraryInfoResponseTO(libraryInfoTO);
        return responseTO;
    }

    @Override
    public BookListResponseTO getBookList(UserInfo userInfo, BooksFilterRequestTO requestTO) {
        LibraryService libraryService = lookup(LibraryService.class);

        String searchList = requestTO.get(SEARCH_PARAM_NAME);
        String searchValue = requestTO.get(SEARCH_PARAM_VALUE);

        String yearFrom = requestTO.get(YEAR_FROM_PARAM);
        String yearTo = requestTO.get(YEAR_TO_PARAM);
        String genre = requestTO.get(GENRE_PARAM);
        if (GENRE_PARAM_ALL.equals(genre)) {
            genre = null;
        }

        BookListResponseTO responseTO = populateBookListResponseTO(libraryService.findBooks(searchList, searchValue, yearFrom, yearTo, genre));

        return responseTO;
    }

    @Override
    public BookInfoResponseTO getBookInfo(UserInfo userInfo, BookIdRequestTO requestTO) {
        BookInfoResponseTO responseTO = new BookInfoResponseTO();

        return responseTO;
    }

    private LibraryInfoResponseTO populateLibraryInfoResponseTO(LibraryInfoTO libraryInfoTO) {
        LibraryInfoResponseTO responseTO = new LibraryInfoResponseTO();
        for (OpenBookTO openBookTO : libraryInfoTO.getOpenBooks()) {
            LibraryInfoResponseTO.OpenBook openBook = new LibraryInfoResponseTO.OpenBook();
            openBook.setId(openBookTO.getBookTO().getId());
            openBook.setName(openBookTO.getBookTO().getName());
            openBook.setAuthor(openBookTO.getBookTO().getAuthor());
            openBook.setPublishedYear(openBookTO.getBookTO().getPublishedYear());
            openBook.setPageCount(openBookTO.getBookTO().getPageCount());
            openBook.setPageNo(openBookTO.getPageNo());
            openBook.setIsDone(openBookTO.isDone());
            openBook.setFirstOpenDateGMT(new Date(openBookTO.getFirstOpenDateGMT()));
            openBook.setLastOpenDateGMT(new Date(openBookTO.getLastOpenDateGMT()));
            responseTO.addOpenBook(openBook);
        }

        return responseTO;
    }

    private BookListResponseTO populateBookListResponseTO(List<BookTO> books) {
        BookListResponseTO responseTO = new BookListResponseTO();
        for (BookTO bookTO : books) {
            BookListResponseTO.Book book = new BookListResponseTO.Book();
            book.setId(bookTO.getId());
            book.setAuthor(bookTO.getAuthor());
            book.setName(bookTO.getName());
            book.setPublishedYear(bookTO.getPublishedYear());
            book.setPageCount(bookTO.getPageCount());
            responseTO.addBook(book);
        }
        return responseTO;
    }
}
