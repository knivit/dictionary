package com.tsoft.dictionary.server.app.service.library;

import com.tsoft.dictionary.server.app.GWTClient;
import java.io.Serializable;

@GWTClient
public class BookTO implements Serializable {
    private String id;
    private String name;
    private String author;
    private int publishedYear;
    private int pageCount;
    private String genre;

    public BookTO() { }

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + id + ", name=" + getName() + ", author=" + getAuthor() + "]";
    }
}
