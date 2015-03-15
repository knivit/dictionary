package com.tsoft.dictionary.server.app.web.tutorial;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class TutorialResponseTO implements IsSerializable {
    public static class Page implements Serializable {
        private String caption;
        private String pageName;
        private int level;

        public Page() { }

        public Page(String caption, String pageName) {
            this.pageName = pageName;

            level = 0;
            for (int i = 0; i < caption.length(); i ++) {
                if (caption.charAt(i) != ' ') {
                    break;
                }
                level ++;
            }
            level >>= 1;

            this.caption = caption.trim();
        }

        public String getCaption() {
            return caption;
        }

        public String getPageName() {
            return pageName;
        }

        public int getLevel() {
            return level;
        }
    }

    private ArrayList<Page> pageList = new ArrayList<Page>();

    public TutorialResponseTO() { }

    public void addHtmlPage(String caption, String pageName) {
        Page htmlPage = new Page(caption, pageName);
        pageList.add(htmlPage);
    }

    public ArrayList<Page> getHtmlPageList() {
        return pageList;
    }
}
