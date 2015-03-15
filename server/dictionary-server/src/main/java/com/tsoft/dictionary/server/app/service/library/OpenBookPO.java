package com.tsoft.dictionary.server.app.service.library;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.tsoft.dictionary.server.app.service.model.IdPO;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries( {
    @NamedQuery(name = "OpenBookPO.findById",
        query =
            "SELECT o FROM com.tsoft.dictionary.server.app.service.library.OpenBookPO o " +
            "WHERE o.id = :openBookId")
})
public class OpenBookPO extends IdPO<OpenBookTO> {
    @ManyToOne
    private Key book;
    private int pageNo;
    private long firstOpenDateGMT;
    private long lastOpenDateGMT;
    private boolean done;

    public OpenBookPO() { }

    @Override
    public void getTOValues(OpenBookTO to) {
        setBook(KeyFactory.stringToKey(to.getBookTO().getId()));
        setPageNo(to.getPageNo());
        setFirstOpenDateGMT(to.getFirstOpenDateGMT());
        setLastOpenDateGMT(to.getLastOpenDateGMT());
        setDone(to.isDone());
    }

    @Override
    public void setTOValues(OpenBookTO to) {
        to.setId(getIdAsString());
        BookTO bookTO = new BookTO();
        getBookPO().setTOValues(bookTO);
        to.setBookTO(bookTO);
        to.setPageNo(getPageNo());
        to.setFirstOpenDateGMT(getFirstOpenDateGMT());
        to.setLastOpenDateGMT(getLastOpenDateGMT());
        to.setDone(isDone());
    }

    public Key getBook() {
        return book;
    }

    public void setBook(Key book) {
        this.book = book;
    }

    public String getBookId() {
        return KeyFactory.keyToString(book);
    }

    public BookPO getBookPO() {
        LibraryService libraryService = lookup(LibraryService.class);
        BookPO bookPO = libraryService.findBookPO(getBook());
        return bookPO;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getFirstOpenDateGMT() {
        return firstOpenDateGMT;
    }

    public void setFirstOpenDateGMT(long firstOpenDateGMT) {
        this.firstOpenDateGMT = firstOpenDateGMT;
    }

    public long getLastOpenDateGMT() {
        return lastOpenDateGMT;
    }

    public void setLastOpenDateGMT(long lastOpenDateGMT) {
        this.lastOpenDateGMT = lastOpenDateGMT;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpenBookPO other = (OpenBookPO) obj;
        if (this.book != other.book && (this.book == null || !this.book.equals(other.book))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.book != null ? this.book.hashCode() : 0);
        return hash;
    }
}
