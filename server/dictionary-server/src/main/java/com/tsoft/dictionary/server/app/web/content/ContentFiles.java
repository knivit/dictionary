package com.tsoft.dictionary.server.app.web.content;

import com.tsoft.dictionary.server.context.ContextPersistent;
import com.tsoft.dictionary.util.file.DirScanner;
import com.tsoft.dictionary.util.file.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletContextEvent;

public class ContentFiles implements ContextPersistent {
    private String folderName;
    private HashMap<String, String> files = new HashMap<String, String>();

    public void load(String folderName) {
        this.folderName = folderName;

        DirScanner fileScanner = new DirScanner(folderName);
        fileScanner.setRecursive(true);
        fileScanner.setAddFile(true);
        fileScanner.scan("*.html");
        ArrayList<String> fileList = fileScanner.getNames();

        files = FileUtil.getFilesMap(fileList);
    }

    public String getAbsoluteFileName(String fileName) {
        return folderName + files.get(fileName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        files.clear();
    }
}
