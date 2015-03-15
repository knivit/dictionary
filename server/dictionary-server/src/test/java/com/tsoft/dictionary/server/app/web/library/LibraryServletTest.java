package com.tsoft.dictionary.server.app.web.library;

import com.tsoft.dictionary.server.app.factory.LibraryTestFactory;
import com.tsoft.dictionary.server.app.Inject;
import com.tsoft.dictionary.server.app.service.library.BookPO;
import com.tsoft.dictionary.server.app.service.library.BookTO;
import com.tsoft.dictionary.server.app.service.library.LibraryInfoPO;
import com.tsoft.dictionary.server.app.service.library.LibraryInfoTO;
import com.tsoft.dictionary.server.app.service.library.OpenBookPO;
import com.tsoft.dictionary.server.app.service.library.OpenBookTO;
import com.tsoft.dictionary.server.app.web.AbstractServletTest;
import com.tsoft.dictionary.server.app.web.TestHelper;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LibraryServletTest extends AbstractServletTest {
    @Inject
    public LibraryServlet libraryServlet;

    @Test
    public void getSearchParameters() {
        BookParametersResponseTO responseTO = libraryServlet.getSearchParameters(TestHelper.getDefaultUserInfo());
        
        assertNotNull(responseTO.getParameters());
    }

    @Test
    public void getLibraryInfo() throws Exception {
        LibraryTestFactory tf = new LibraryTestFactory(TestHelper.getDefaultUserInfo());

        BookTO bookTO1 = tf.createBook();
        BookPO bookPO1 = tf.createPersistedBook(bookTO1);
        OpenBookTO openBookTO1 = tf.createOpenBook(bookPO1);
        OpenBookPO openBookPO1 = tf.createPersistedOpenBook(openBookTO1);

        BookTO bookTO2 = tf.createBook();
        BookPO bookPO2 = tf.createPersistedBook(bookTO2);
        OpenBookTO openBookTO2 = tf.createOpenBook(bookPO2);
        OpenBookPO openBookPO2 = tf.createPersistedOpenBook(openBookTO2);

        LibraryInfoTO libraryInfoTO = tf.createLibraryInfo(openBookPO1, openBookPO2);
        LibraryInfoPO libraryInfoPO = tf.createPersistedLibraryInfo(libraryInfoTO);

        LibraryInfoResponseTO responseTO = libraryServlet.getLibraryInfo(TestHelper.getDefaultUserInfo());

        assertNotNull(responseTO.getOpenBooks());
        assertEquals(2, responseTO.getOpenBooks().size());
    }

    @Test
    public void getBookList() throws Exception {
        LibraryTestFactory tf = new LibraryTestFactory(TestHelper.getDefaultUserInfo());

        BookTO bookTO1 = tf.createBook();
        bookTO1.setAuthor("andy");
        BookPO bookPO1 = tf.createPersistedBook(bookTO1);

        BooksFilterRequestTO requestTO = new BooksFilterRequestTO();
        requestTO.add(LibraryServlet.SEARCH_PARAM_NAME, LibraryServlet.SEARCH_PARAM_AUTHOR);
        requestTO.add(LibraryServlet.SEARCH_PARAM_VALUE, "andy");
        BookListResponseTO responseTO = libraryServlet.getBookList(TestHelper.getDefaultUserInfo(), requestTO);
        assertNotNull(responseTO.getBooks());
        assertEquals(1, responseTO.getBooks().size());

        requestTO = new BooksFilterRequestTO();
        requestTO.add(LibraryServlet.SEARCH_PARAM_NAME, LibraryServlet.SEARCH_PARAM_AUTHOR);
        requestTO.add(LibraryServlet.SEARCH_PARAM_VALUE, "AnDy");
        responseTO = libraryServlet.getBookList(TestHelper.getDefaultUserInfo(), requestTO);
        assertNotNull(responseTO.getBooks());
        assertEquals(1, responseTO.getBooks().size());

        BookTO bookTO2 = tf.createBook();
        bookTO2.setName("java language");
        bookTO2.setAuthor("andy rolson");
        BookPO bookPO2 = tf.createPersistedBook(bookTO2);

        requestTO = new BooksFilterRequestTO();
        requestTO.add(LibraryServlet.SEARCH_PARAM_NAME, LibraryServlet.SEARCH_PARAM_AUTHOR);
        requestTO.add(LibraryServlet.SEARCH_PARAM_VALUE, "andy");
        responseTO = libraryServlet.getBookList(TestHelper.getDefaultUserInfo(), requestTO);
        assertNotNull(responseTO.getBooks());
        assertEquals(2, responseTO.getBooks().size());

        requestTO = new BooksFilterRequestTO();
        requestTO.add(LibraryServlet.SEARCH_PARAM_NAME, LibraryServlet.SEARCH_PARAM_TITLE);
        requestTO.add(LibraryServlet.SEARCH_PARAM_VALUE, "pascal");
        responseTO = libraryServlet.getBookList(TestHelper.getDefaultUserInfo(), requestTO);
        assertNotNull(responseTO.getBooks());
        assertEquals(0, responseTO.getBooks().size());
    }
}
