package com.tsoft.dictionary.client.widget;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public final class TreeHelper {
    private TreeHelper() { }

    public static TreeItem findTreeItem(Tree tree, Object userObject) {
        for (int i = 0; i < tree.getItemCount(); i ++) {
            TreeItem item = findTreeItem(tree.getItem(i), userObject);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    private static TreeItem findTreeItem(TreeItem item, Object userObject) {
        if (userObject.equals(item.getUserObject())) {
            return item;
        }

        for (int i = 0; i < item.getChildCount(); i ++) {
            TreeItem child = findTreeItem(item.getChild(i), userObject);
            if (child != null) {
                return child;
            }
        }

        return null;
    }

    public static void selectTreeItem(TreeItem item) {
        TreeItem parentItem = item;
        while (true) {
            parentItem = parentItem.getParentItem();
            if (parentItem != null) {
                parentItem.setState(true);
            } else {
                break;
            }
        }
        item.getTree().setSelectedItem(item);
    }
}
