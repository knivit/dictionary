package com.tsoft.dictionary.server.app.factory;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.service.library.BookPO;
import com.tsoft.dictionary.server.app.service.library.BookTO;
import com.tsoft.dictionary.server.app.service.library.LibraryInfoPO;
import com.tsoft.dictionary.server.app.service.library.LibraryInfoTO;
import com.tsoft.dictionary.server.app.service.library.OpenBookPO;
import com.tsoft.dictionary.server.app.service.library.OpenBookTO;
import com.tsoft.dictionary.server.app.web.TestHelper;
import com.tsoft.dictionary.server.app.web.library.LibraryServlet;
import com.tsoft.dictionary.server.util.DateHelper;
import java.util.HashSet;
import java.util.Set;

public class LibraryTestFactory extends TestFactory {
    public LibraryTestFactory(UserInfo userInfo) {
        super(userInfo);
    }

    public BookTO createBook() {
        BookTO to = new BookTO();
        to.setName(TestHelper.getUniqueString());
        to.setAuthor(TestHelper.getUniqueString());
        to.setPublishedYear(TestHelper.getRandomInteger(1900, 2000));
        to.setPageCount(TestHelper.getRandomInteger(10, 1500));
        to.setGenre(TestHelper.getRandomElement(LibraryServlet.GENRE_PARAM_VALUES));

        return to;
    }

    public BookPO createPersistedBook(BookTO to) throws Exception {
        BookPO po = new BookPO();
        po.getTOValues(to);
        DatastoreHelper.persist(po);
        return po;
    }

    public OpenBookTO createOpenBook(BookPO bookPO) {
        OpenBookTO to = new OpenBookTO();
        BookTO bookTO = new BookTO();
        bookPO.setTOValues(bookTO);
        to.setBookTO(bookTO);
        to.setFirstOpenDateGMT(DateHelper.getCurrentDateGMTAsLong());
        to.setLastOpenDateGMT(DateHelper.getCurrentDateGMTAsLong());
        to.setPageNo(1);
        to.setDone(false);
        return to;
    }

    public OpenBookPO createPersistedOpenBook(OpenBookTO to) throws Exception {
        OpenBookPO po = new OpenBookPO();
        po.getTOValues(to);
        DatastoreHelper.persist(po);
        return po;
    }

    public LibraryInfoTO createLibraryInfo(OpenBookPO ... openBooksPO) {
        LibraryInfoTO to = new LibraryInfoTO();
        to.setUserId(getUserInfo().getId());
        Set<OpenBookTO> openBooks = new HashSet<OpenBookTO>();
        if (openBooksPO != null) {
            for (OpenBookPO openBookPO : openBooksPO) {
                OpenBookTO openBookTO = new OpenBookTO();
                openBookPO.setTOValues(openBookTO);
                openBooks.add(openBookTO);
            }
        }
        to.setOpenBooks(openBooks);
        return to;
    }

    public LibraryInfoPO createPersistedLibraryInfo(LibraryInfoTO to) throws Exception {
        LibraryInfoPO po = new LibraryInfoPO();
        po.getTOValues(to);
        DatastoreHelper.persist(po);
        return po;
    }

    /**
     * Fill a database with generated data
     *
     * @param bookCount Number of books
     * @param userInfo An user (may be TestHelper.getDefaultUserInfo())
     * @param openBookCount Number of open books by given user
     */
    public static void generateTestData(int bookCount, UserInfo userInfo, int openBookCount) throws Exception {
        LibraryTestFactory tf = new LibraryTestFactory(userInfo);

        OpenBookPO[] openBookPOs = new OpenBookPO[openBookCount];
        for (int i = 0; i < bookCount; i ++) {
            BookTO bookTO = tf.createBook();
            BookPO bookPO = tf.createPersistedBook(bookTO);

            if (openBookCount > 0) {
                OpenBookTO openBookTO = tf.createOpenBook(bookPO);
                OpenBookPO openBookPO = tf.createPersistedOpenBook(openBookTO);

                openBookPOs[i] = openBookPO;

                openBookCount --;
            }
        }

        LibraryInfoTO libraryInfoTO = tf.createLibraryInfo(openBookPOs);
        LibraryInfoPO libraryInfoPO = tf.createPersistedLibraryInfo(libraryInfoTO);
    }
}
