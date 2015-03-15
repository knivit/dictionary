package com.tsoft.dictionary.server.app.service.library;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LibraryInfoTO implements Serializable {
    private String userId;
    private Set<OpenBookTO> openBooks = new HashSet<OpenBookTO>();

    public LibraryInfoTO() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<OpenBookTO> getOpenBooks() {
        return openBooks;
    }

    public void setOpenBooks(Set<OpenBookTO> openBooks) {
        this.openBooks.clear();
        this.openBooks.addAll(openBooks);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[userId=" + userId + ", openBooks.size()=" + openBooks.size() + "]";
    }
}