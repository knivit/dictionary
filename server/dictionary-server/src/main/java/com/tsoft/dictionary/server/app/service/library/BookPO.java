package com.tsoft.dictionary.server.app.service.library;

import com.tsoft.dictionary.server.app.service.model.IdPO;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries( {
    @NamedQuery(name = "BookPO.findById",
        query =
            "SELECT b FROM com.tsoft.dictionary.server.app.service.library.BookPO b " +
            "WHERE b.id = :bookId"),

    @NamedQuery(name = "BookPO.findByIds",
        query =
            "SELECT b FROM com.tsoft.dictionary.server.app.service.library.BookPO b " +
            "WHERE b.id in (:ids) " +
            "ORDER BY b.author, b.name")
})
public class BookPO extends IdPO<BookTO> {
    private String name;
    private String author;
    private int publishedYear;
    private int pageCount;
    private String genre;

    public BookPO() { }

    @Override
    public void getTOValues(BookTO to) {
        setName(to.getName());
        setAuthor(to.getAuthor());
        setPublishedYear(to.getPublishedYear());
        setPageCount(to.getPageCount());
    }

    @Override
    public void setTOValues(BookTO to) {
        to.setId(getIdAsString());
        to.setName(getName());
        to.setAuthor(getAuthor());
        to.setPublishedYear(getPublishedYear());
        to.setPageCount(getPageCount());
        to.setGenre(getGenre());
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
        return getClass().getName() + "[id=" + getId().toString() + "]";
    }
}
