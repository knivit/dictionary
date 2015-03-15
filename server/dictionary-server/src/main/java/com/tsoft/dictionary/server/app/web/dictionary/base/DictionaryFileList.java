package com.tsoft.dictionary.server.app.web.dictionary.base;

import com.tsoft.dictionary.server.context.ContextPersistent;
import com.tsoft.dictionary.util.file.DirScanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class DictionaryFileList implements ContextPersistent {
    private static final Logger logger = Logger.getLogger(DictionaryFileList.class.getName());

    public static final String DICTIONARY_FILENAME_EXT = ".txt";

    private String baseDir;
    private ArrayList<DictionaryFile> dictionaryList = new ArrayList<DictionaryFile>();
    private ArrayList<String> nameList = new ArrayList<String>();

    public DictionaryFileList(String baseDir) {
        this.baseDir = baseDir;
    }

    public void load(ServletContext servletContext) {
        logger.log(Level.INFO, "Get the dictionary list from " + baseDir);

        DirScanner files = new DirScanner(baseDir);
        files.setAddFile(true);
        files.scan("*" + DICTIONARY_FILENAME_EXT);
        ArrayList<File> fileList = files.getFiles();
        
        for (File file : fileList) {
            String name = file.getName();
            name = name.substring(0, file.getName().length() - DICTIONARY_FILENAME_EXT.length());
            String fileName = baseDir + File.separator + file.getName();
            DictionaryFile dictionaryFile = new DictionaryFile(name, fileName);
            dictionaryList.add(dictionaryFile);
            nameList.add(name);
        }

        Collections.sort(nameList);
    }
    
    public String getFileName(String dictionaryName) {
        for (DictionaryFile dictionaryFile : dictionaryList) {
            if (dictionaryFile.getName().equals(dictionaryName)) {
                return dictionaryFile.getFileName();
            }
        }
        return null;
    }

    public ArrayList<String> getDictionaryNameList() {
        return nameList;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        dictionaryList.clear();
    }
}
