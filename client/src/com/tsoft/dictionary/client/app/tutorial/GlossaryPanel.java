package com.tsoft.dictionary.client.app.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.tutorial.callback.GlossaryIndexRPC;
import com.tsoft.dictionary.client.app.tutorial.callback.TutorialContentRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.content.ContentRequestTO;
import com.tsoft.dictionary.server.app.web.content.ContentResponseTO;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialResponseTO;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialResponseTO.Page;
import com.tsoft.dictionary.client.widget.TreeHelper;
import java.util.ArrayList;

public class GlossaryPanel extends AbstractPanel<TutorialForm> implements GlossaryIndexRPC, TutorialContentRPC {
    interface Binder extends UiBinder<Widget, GlossaryPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField Tree tree;

    private ArrayList<Page> pageList;

    private String pageName;
    private boolean isContentLoading;

    public GlossaryPanel() {
        super(binder);
    }

    @UiHandler("tree")
    public void onSelection(SelectionEvent<TreeItem> event) {
        if (isContentLoading) {
            return;
        }

        TreeItem selectedItem = event.getSelectedItem();
        Page selectedPage = (Page)selectedItem.getUserObject();

        if (selectedPage != null) {
            getPageContent(selectedPage.getPageName());
        }
    }

    public void openPage(String pageName) {
        Page page = findIndexPage(pageName);
        if (page == null) {
            getPageContent(pageName);
            return;
        }

        TreeItem item = TreeHelper.findTreeItem(tree, page);
        if (item != null) {
            TreeHelper.selectTreeItem(item);
        }
    }

    private void getPageContent(String pageName) {
        if (pageName != null) {
            this.pageName = pageName;
            getForm().getContent();
        }
    }

    private Page findIndexPage(String ref) {
        for (Page page : pageList) {
            if (ref.equals(page.getPageName())) {
                return page;
            }
        }
        return null;
    }

    private void buildIndex(ArrayList<Page> pageList) {
        this.pageList = pageList;

        if (pageList == null) {
            tree.addItem("<No data loaded>");
            return;
        }

        int pos = 0, priorLevel = 0;
        TreeItem[] stack = new TreeItem[16];

        for (Page page : pageList) {
            int level = page.getLevel();
            if (priorLevel < level) {
                pos ++;
            }
            if (priorLevel > level) {
                pos --;
            }

            TreeItem item;
            String itemName = page.getCaption();
            if (level == 0) {
                item = tree.addItem(itemName);
            } else {
                TreeItem parent = stack[pos - 1];
                item = parent.addItem(itemName);
            }
            item.setUserObject(page);
            
            stack[level] = item;
            priorLevel = level;
        }
    }

    @Override
    public void beforeGlossaryIndexRPC(Void requestTO) {
        tree.setVisible(false);
    }

    @Override
    public void afterGlossaryIndexRPC(TutorialResponseTO responseTO, boolean success) {
        buildIndex(success ? responseTO.getHtmlPageList() : null);
        tree.setVisible(true);
    }

    @Override
    public void beforeContentRPC(ContentRequestTO requestTO) {
        requestTO.setContentName("tutorial");
        requestTO.setPageName(pageName);
        isContentLoading = true;
    }

    @Override
    public void afterContentRPC(ContentResponseTO responseTO, boolean isSuccess) {
        isContentLoading = false;
    }
}
