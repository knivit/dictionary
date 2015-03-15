package com.tsoft.dictionary.server.app.web.tutorial;

import com.tsoft.dictionary.server.util.StringHelper;
import com.tsoft.dictionary.server.context.ContextPersistent;
import com.tsoft.dictionary.util.NameValue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;

public class TutorialIndex implements ContextPersistent {
    private static final Logger logger = Logger.getLogger(TutorialIndex.class.getName());

    private ArrayList<NameValue> pageList = new ArrayList<NameValue>();

    public TutorialIndex() { }

    public void load(String path) {
        String indexFileName = path + "/tutorial/index.txt";
        BufferedReader inputStream = null;
        try {
            try {
                inputStream = new BufferedReader(new FileReader(indexFileName));

                String line;
                while ((line = inputStream.readLine()) != null) {
                    if (StringHelper.isEmpty(line)) {
                        continue;
                    }
                    
                    String caption = line;
                    String pageName = null;
                    int n = line.indexOf('*');
                    if (n != -1) {
                        caption = line.substring(0, n);
                        pageName = line.substring(n + 1).trim();
                    }
                    
                    NameValue page = new NameValue(caption, pageName);
                    pageList.add(page);
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Can't open file '" + indexFileName + "'", ex);
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) { }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        pageList.clear();
    }

    public void setValuesTo(TutorialResponseTO responseTO) {
        for (NameValue page : pageList) {
            responseTO.addHtmlPage(page.getName(), page.getValue());
        }
    }
}
