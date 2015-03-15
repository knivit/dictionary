package com.tsoft.dictionary.client.server;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.login.LoginServletInterface;
import com.tsoft.dictionary.client.server.app.LoginServletProxy;
import com.tsoft.dictionary.client.server.app.TutorialServletProxy;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialServletInterface;
import com.tsoft.dictionary.client.server.app.LibraryServletProxy;
import com.tsoft.dictionary.server.app.web.library.LibraryServletInterface;
import com.tsoft.dictionary.client.server.app.DictionaryServletProxy;
import com.tsoft.dictionary.server.app.web.dictionary.DictionaryServletInterface;
import com.tsoft.dictionary.client.server.app.ContentServletProxy;
import com.tsoft.dictionary.server.app.web.content.ContentServletInterface;
import com.tsoft.dictionary.server.app.web.wordtrainer.WordTrainerServletInterface;
import com.tsoft.dictionary.client.server.app.WordTrainerServletProxy;
import com.google.gwt.core.client.GWT;
import com.tsoft.dictionary.server.app.web.content.ContentServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.dictionary.DictionaryServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.library.LibraryServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.login.LoginServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.wordtrainer.WordTrainerServletInterfaceAsync;

public final class ServerServlets {
    private LoginServletProxy loginServlet;
    private WordTrainerServletProxy wordTrainerServlet;
    private DictionaryServletProxy dictionaryServlet;
    private ContentServletProxy contentServlet;
    private TutorialServletProxy tutorialServlet;
    private LibraryServletProxy libraryServlet;

    private UserInfo userInfo;

    public ServerServlets(UserInfo userInfo) {
        setUserInfo(userInfo);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public synchronized LoginServletProxy getLoginServlet() {
        if (loginServlet == null) {
            LoginServletInterfaceAsync servletAsync = GWT.create(LoginServletInterface.class);
            loginServlet = new LoginServletProxy(this, servletAsync);
        }
        return loginServlet;
    }

    public synchronized WordTrainerServletProxy getWordTrainerServlet() {
        if (wordTrainerServlet == null) {
            WordTrainerServletInterfaceAsync servletAsync = GWT.create(WordTrainerServletInterface.class);
            wordTrainerServlet = new WordTrainerServletProxy(this, servletAsync);
        }
        return wordTrainerServlet;
    }

    public synchronized DictionaryServletProxy getDictionaryServlet() {
        if (dictionaryServlet == null) {
            DictionaryServletInterfaceAsync servletAsync = GWT.create(DictionaryServletInterface.class);
            dictionaryServlet = new DictionaryServletProxy(this, servletAsync);
        }
        return dictionaryServlet;
    }

    public synchronized ContentServletProxy getContentServlet() {
        if (contentServlet == null) {
            ContentServletInterfaceAsync servletAsync = GWT.create(ContentServletInterface.class);
            contentServlet = new ContentServletProxy(this, servletAsync);
        }
        return contentServlet;
    }

    public synchronized TutorialServletProxy getTutorialServlet() {
        if (tutorialServlet == null) {
            TutorialServletInterfaceAsync servletAsync = GWT.create(TutorialServletInterface.class);
            tutorialServlet = new TutorialServletProxy(this, servletAsync);
        }
        return tutorialServlet;
    }

    public synchronized LibraryServletProxy getLibraryServlet() {
        if (libraryServlet == null) {
            LibraryServletInterfaceAsync servletAsync = GWT.create(LibraryServletInterface.class);
            libraryServlet = new LibraryServletProxy(this, servletAsync);
        }
        return libraryServlet;
    }
}
