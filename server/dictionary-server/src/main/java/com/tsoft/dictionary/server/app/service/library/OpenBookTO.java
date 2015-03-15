package com.tsoft.dictionary.server.app.service.library;

import com.tsoft.dictionary.server.app.GWTClient;
import java.io.Serializable;

@GWTClient
public class OpenBookTO implements Serializable {
    private String id;
    private BookTO bookTO = new BookTO();
    private int pageNo;
    private long firstOpenDateGMT;
    private long lastOpenDateGMT;
    private boolean done;

    public OpenBookTO() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookTO getBookTO() {
        return bookTO;
    }

    public void setBookTO(BookTO bookTO) {
        this.bookTO = bookTO;
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
        final OpenBookTO other = (OpenBookTO) obj;
        if ((this.getBookTO().getId() == null) ? (other.getBookTO().getId() != null) : !this.getBookTO().getId().equals(other.getBookTO().getId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return getBookTO().getId().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[bookId=" + getBookTO().getId() + ",isDone=" + isDone() + "]";
    }
}
