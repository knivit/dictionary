package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@GWTClient
public class BookListResponseTO implements IsSerializable {
    private boolean isSorted;

    public static class Book implements Serializable {
        private String id;
        private String name;
        private String author;
        private int publishedYear;
        private int pageCount;

        public Book() { }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
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

        public int getPublishedYear() {
            return publishedYear;
        }

        public void setPublishedYear(int publishedYear) {
            this.publishedYear = publishedYear;
        }
    }

    private ArrayList<Book> books = new ArrayList<Book>();

    public BookListResponseTO() { }

    public void addBook(Book book) {
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        if (!isSorted) {
            sort();
        }

        return books;
    }

    private void sort() {
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                if (b1.getAuthor().equalsIgnoreCase(b2.getAuthor())) {
                    if (b1.getPublishedYear() == b2.getPublishedYear()) {
                        return 0;
                    }

                    return b1.getPublishedYear() < b2.getPublishedYear() ? -1 : 1;
                }

                return b1.getAuthor().compareToIgnoreCase(b2.getAuthor());
            }
        });

        isSorted = true;
    }
}
