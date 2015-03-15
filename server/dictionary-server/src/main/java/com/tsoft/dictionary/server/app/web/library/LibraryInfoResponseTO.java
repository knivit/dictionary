package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

@GWTClient
public class LibraryInfoResponseTO implements IsSerializable {
    private boolean isSorted;

    public static class OpenBook implements Serializable {
        private String id;
        private String name;
        private String author;
        private int publishedYear;
        private int pageCount;
        private int pageNo;
        private boolean isDone;
        private Date firstOpenDateGMT;
        private Date lastOpenDateGMT;

        public OpenBook() { }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getFirstOpenDateGMT() {
            return firstOpenDateGMT;
        }

        public void setFirstOpenDateGMT(Date firstOpenDateGMT) {
            this.firstOpenDateGMT = firstOpenDateGMT;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIsDone() {
            return isDone;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }

        public Date getLastOpenDateGMT() {
            return lastOpenDateGMT;
        }

        public void setLastOpenDateGMT(Date lastOpenDateGMT) {
            this.lastOpenDateGMT = lastOpenDateGMT;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPublishedYear() {
            return publishedYear;
        }

        public void setPublishedYear(int publishedYear) {
            this.publishedYear = publishedYear;
        }

    }

    private ArrayList<OpenBook> openBooks = new ArrayList<OpenBook>();

    public LibraryInfoResponseTO() { }

    public void addOpenBook(OpenBook openBook) {
        openBooks.add(openBook);
    }

    public ArrayList<OpenBook> getOpenBooks() {
        if (!isSorted) {
            sort();
        }
        
        return openBooks;
    }

    private void sort() {
        Collections.sort(openBooks, new Comparator<OpenBook>() {
            @Override
            public int compare(OpenBook b1, OpenBook b2) {
                return b1.getLastOpenDateGMT().compareTo(b2.getLastOpenDateGMT());
            }
        });

        isSorted = true;
    }
}
