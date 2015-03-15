package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class BookInfoResponseTO implements IsSerializable {
    private String id;
    private String title;
    private ArrayList<String> top100Words;
    private ArrayList<Author> authors;

    public static class Author implements Serializable {
        private String name;
        private String wikiUrl;

        public Author() { }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWikiUrl() {
            return wikiUrl;
        }

        public void setWikiUrl(String wikiUrl) {
            this.wikiUrl = wikiUrl;
        }
    }

    public BookInfoResponseTO( ) { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getTop100Words() {
        return top100Words;
    }

    public void setTop100Words(ArrayList<String> top100Words) {
        this.top100Words = top100Words;
    }
}
