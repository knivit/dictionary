package com.tsoft.dictionary.client;

import com.allen_sauer.gwt.log.client.Log;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.client.app.main.MainForm;
import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;

/*
 * See us at http://webtsoft.appspot.com !
 *
 * https://appengine.google.com/start/createapp_success?app_id=webtsoft
 * http://code.google.com/intl/ru-RU/appengine/docs/java/tools/uploadinganapp.html
 * http://code.google.com/intl/ru-RU/appengine/docs/appcfg.html
 * http://code.google.com/intl/ru-RU/appengine/downloads.html#Google_App_Engine_SDK_for_Java
 * http://code.google.com/intl/en/webtoolkit/doc/latest/tutorial/appengine.html
 *
 * http://mojo.codehaus.org/gwt-maven-plugin/gwt20.html
 *
 * SmartGWT
 * http://code.google.com/p/smartgwt/
 *
 * JavaDoc
 * http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/index.html?overview-summary.html
 *
 * Blogs
 * http://gae-java-persistence.blogspot.com/2009/10/creating-bidrectional-owned-one-to-many.html
 *
 * TODO:
 * Add :
 * 1) Tutorials with Verb Tense Tutorial etc
 *    http://www.englishpage.com/verbpage/pastperfect.html
 *    http://www.englishpage.com/modals/might.html
 * 2) Grammar
 *    http://www.englisch-hilfen.de/en/exercises_list/alle_grammar.htm
*/
public class Main implements EntryPoint {
    private ServerServlets serverServlets = new ServerServlets(UserInfo.UNDEFINED_USERINFO);

    interface GlobalResources extends ClientBundle {
        @NotStrict
        @Source("global.css")
        CssResource css();
    }

    public Main() { }

    @Override
    public void onModuleLoad() {
        Log.setUncaughtExceptionHandler();
        getUserInfo();
    }

    private class UserInfoCallback extends DefaultAsyncCallback<Void, UserInfoResponseTO> {
        public UserInfoCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) { }

        @Override
        public void afterRemoteCall(UserInfoResponseTO responseTO, boolean isSuccess) {
            UserInfo userInfo;
            if (isSuccess) {
                userInfo = new UserInfo(responseTO);
            } else {
                userInfo = UserInfo.UNDEFINED_USERINFO;
            }
            serverServlets.setUserInfo(userInfo);

            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    start();
                }
            });
        }
    }

    public void getUserInfo() {
        UserInfoCallback callback = new UserInfoCallback(null);
        serverServlets.getLoginServlet().getUserInfo(callback);
    }

    private void start() {
        // Inject global styles
        GWT.<GlobalResources>create(GlobalResources.class).css().ensureInjected();

        // Get rid of scrollbars, and clear out the window's built-in margin,
        // because we want to take advantage of the entire client area
        Window.enableScrolling(false);
        Window.setMargin("0px");

        MainForm mainForm = new MainForm(serverServlets);
        RootLayoutPanel root = RootLayoutPanel.get();
        root.add(mainForm.getFormPanel());
    }
}
