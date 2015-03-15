package com.tsoft.dictionary.server.app.service.library;

import com.google.appengine.api.datastore.Key;
import com.tsoft.dictionary.server.app.Inject;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.library.LibraryServlet;
import com.tsoft.dictionary.server.util.StringHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class LibraryServiceBean implements LibraryService {
    private static final Logger logger = Logger.getLogger(LibraryServiceBean.class.getName());

    @Inject
    public EntityManager em;

    @Override
    public LibraryInfoTO getLibraryInfo(UserInfo userInfo) {
        Query query = em.createNamedQuery("LibraryInfoPO.findByUser");
        query.setParameter("userId", userInfo.getId());

        LibraryInfoPO po = null;
        try {
            po = (LibraryInfoPO) query.getSingleResult();
        } catch (NoResultException ex) { }

        LibraryInfoTO libraryInfoTO = new LibraryInfoTO();
        po.setTOValues(libraryInfoTO);
        return libraryInfoTO;
    }

    @Override
    public List<BookTO> findBooks(String searchList, String searchValue, String yearFrom, String yearTo, String genre) {
        ArrayList<BookTO> bookList = new ArrayList<BookTO>();
        
        List<BookPO> list = null;
        String str = buildfindBooksQuery(searchList, searchValue, yearFrom, yearTo, genre);
        logger.info(str);

        Query query = em.createQuery(str);
        try {
            list = query.getResultList();
        } catch (NoResultException ex) { }

        if (list != null) {
            for (BookPO po : list) {
                BookTO to = new BookTO();
                po.setTOValues(to);
                bookList.add(to);
            }
        }
        
        return bookList;
    }

    private String buildfindBooksQuery(String searchList, String searchValue, String yearFrom, String yearTo, String genre) {
        String searchFieldName = null;
        boolean isSearchListEmpty = StringHelper.isEmpty(searchList);
        if (!isSearchListEmpty) {
            if (LibraryServlet.SEARCH_PARAM_TITLE.equals(searchList)) {
                searchFieldName = "name";
            } else {
            if (LibraryServlet.SEARCH_PARAM_AUTHOR.equals(searchList)) {
                searchFieldName = "author";
            } else
            if (LibraryServlet.SEARCH_PARAM_CONTENT.equals(searchList)) {
                searchFieldName = "<not implemented>";
            } else
                throw new IllegalArgumentException("Unknown Search List Parameter");
            }

            boolean isSearchValueEmpty = StringHelper.isEmpty(searchValue);
            if (!isSearchValueEmpty) {
                searchValue = StringHelper.getPatternValue(searchValue);
            } else {
                isSearchListEmpty = true;
            }
        }

        boolean isYearFromEmpty = StringHelper.isEmpty(yearFrom);
        boolean isYearToEmpty = StringHelper.isEmpty(yearTo);
        boolean isGenreEmpty = StringHelper.isEmpty(genre);

        StringBuilder whereStr = new StringBuilder();
        if (!isSearchListEmpty) {
            if (whereStr.length() > 0) {
                whereStr.append(" AND ");
            }
            whereStr.append("b.").append(searchFieldName).append(" LIKE '").append(searchValue).append("'");
        }

        if (!isYearFromEmpty) {
            if (whereStr.length() > 0) {
                whereStr.append(" AND ");
            }

            if (!isYearToEmpty) {
                whereStr.append("b.publishedYear BETWEEN ").append(yearFrom).append(" AND ").append(yearTo);
            } else {
                whereStr.append("b.publishedYear >= ").append(yearFrom);
            }
        } else {
            if (!isYearToEmpty) {
                if (whereStr.length() > 0) {
                    whereStr.append(" AND ");
                }
                whereStr.append("b.publishedYear <= ").append(yearTo);
            }
        }

        if (!isGenreEmpty) {
            if (whereStr.length() > 0) {
                whereStr.append(" AND ");
            }
            whereStr.append("b.genre = '").append(genre).append("'");
        }

        StringBuilder queryStr = new StringBuilder();
        queryStr.append("SELECT b FROM com.tsoft.dictionary.server.app.service.library.BookPO b ");
        queryStr.append("WHERE ").append(whereStr);

        return queryStr.toString();
    }

    @Override
    public BookPO findBookPO(Key id) {
        Query query = em.createNamedQuery("BookPO.findById");
        query.setParameter("bookId", id);

        BookPO po = null;
        try {
            po = (BookPO) query.getSingleResult();
        } catch (NoResultException ex) { }

        return po;
    }

    @Override
    public OpenBookPO findOpenBookPO(Key id) {
        Query query = em.createNamedQuery("OpenBookPO.findById");
        query.setParameter("openBookId", id);

        OpenBookPO po = null;
        try {
            po = (OpenBookPO) query.getSingleResult();
        } catch (NoResultException ex) { }

        return po;
    }
}
