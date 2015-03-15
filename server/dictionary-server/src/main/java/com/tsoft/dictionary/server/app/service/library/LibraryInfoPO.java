package com.tsoft.dictionary.server.app.service.library;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.tsoft.dictionary.server.app.service.model.UserPO;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries( {
    @NamedQuery(name = "LibraryInfoPO.findByUser",
        query =
            "SELECT i FROM com.tsoft.dictionary.server.app.service.library.LibraryInfoPO i " +
            "WHERE i.userId = :userId")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class LibraryInfoPO extends UserPO<LibraryInfoTO> {
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Key> openBooks = new HashSet<Key>();

    public LibraryInfoPO() { }

    @Override
    public void getTOValues(LibraryInfoTO to) {
        setUserId(to.getUserId());
        Set<Key> openBookKeys = new HashSet<Key>();
        for (OpenBookTO openBookTO : to.getOpenBooks()) {
            Key openBookKey = KeyFactory.stringToKey(openBookTO.getId());
            openBookKeys.add(openBookKey);
        }
        setOpenBooks(openBookKeys);
    }

    @Override
    public void setTOValues(LibraryInfoTO to) {
        to.setUserId(getUserId());
        to.getOpenBooks().clear();
        for (OpenBookPO openBookPO : getOpenBookPOs()) {
            OpenBookTO openBookTO = new OpenBookTO();
            openBookPO.setTOValues(openBookTO);
            to.getOpenBooks().add(openBookTO);
        }
    }

    public Set<Key> getOpenBooks() {
        return openBooks;
    }

    public void setOpenBooks(Set<Key> openBooks) {
        this.openBooks.clear();
        this.openBooks.addAll(openBooks);
    }

    public Set<OpenBookPO> getOpenBookPOs() {
        LibraryService libraryService = lookup(LibraryService.class);
        Set<OpenBookPO> openBookPOs = new HashSet<OpenBookPO>();
        for (Key openBookKey : openBooks) {
            OpenBookPO openBookPO = libraryService.findOpenBookPO(openBookKey);
            openBookPOs.add(openBookPO);
        }
        return openBookPOs;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + getId().toString() + ", userId=" + getUserId() + "]";
    }
}
