package com.tsoft.dictionary.server.app.web.dictionary.base;

import com.tsoft.dictionary.api.Base;
import com.tsoft.dictionary.api.txt.TxtBase;
import com.tsoft.dictionary.server.context.ContextPersistent;
import com.tsoft.dictionary.server.context.ServletContextHelper;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class BaseHelper {
    private static final Logger logger = Logger.getLogger(BaseHelper.class.getName());

    private static final String BASE_DIR_NAME = "/base";

    private BaseHelper() { }

    public static class ContextPersistentTxtBase extends TxtBase implements ContextPersistent {
        @Override
        public void contextDestroyed(ServletContextEvent event) {
            close();
        }
    }

    public static synchronized Base getBase(ServletContext servletContext, String dictionaryName) {
        ContextPersistentTxtBase base = ServletContextHelper.getInstance().getFromContext(servletContext, ContextPersistentTxtBase.class, dictionaryName);
        if (base != null) {
            return base;
        }

        base = new ContextPersistentTxtBase();
        DictionaryFileList dictionaryFileList = getDictionaryFileList(servletContext);
        String fileName = dictionaryFileList.getFileName(dictionaryName);
        
        // dictionary with given name doesn't exists anymore
        if (fileName == null) {
            logger.log(Level.SEVERE, "Dictionary '" + dictionaryName + "' not found");
            throw new IllegalStateException();
        }

        try {
            base.setFileName(fileName);
            ServletContextHelper.getInstance().putToContext(servletContext, base, dictionaryName);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't open the dictionary file: " + fileName, ex);
            throw new IllegalStateException();
        }
        return base;
    }

    public static synchronized ArrayList<String> getDictionaryList(ServletContext servletContext) {
        DictionaryFileList dictionaryList = getDictionaryFileList(servletContext);
        return dictionaryList.getDictionaryNameList();
    }

    private static synchronized DictionaryFileList getDictionaryFileList(ServletContext servletContext) {
        DictionaryFileList dictionaryList = ServletContextHelper.getInstance().getFromContext(servletContext, DictionaryFileList.class);
        if (dictionaryList != null) {
            return dictionaryList;
        }

        String baseDirPath = servletContext.getRealPath(BASE_DIR_NAME);
        dictionaryList = new DictionaryFileList(baseDirPath);
        dictionaryList.load(servletContext);

        ServletContextHelper.getInstance().putToContext(servletContext, dictionaryList);
        return dictionaryList;
    }
}
