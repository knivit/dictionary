package com.tsoft.dictionary.server.app.web.content;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.context.ServletContextHelper;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContentServlet extends RemoteServiceServlet implements ContentServletInterface {
    private static final Logger logger = Logger.getLogger(ContentServlet.class.getName());

    @Override
    public ContentResponseTO getContent(UserInfo userInfo, ContentRequestTO requestTO) {
        ContentResponseTO responseTO = new ContentResponseTO();
        responseTO.setPageName(requestTO.getPageName());

        ContentFiles contentFiles = getContentFiles(requestTO.getContentName());
        File file = new File(contentFiles.getAbsoluteFileName(requestTO.getPageName()));
        responseTO.setContent(getFileContent(file));

        return responseTO;
    }

    private String getFileContent(File file) {
        FileReader rd = null;
        try {
            try {
                rd = new FileReader(file);
                char[] buf = new char[(int)file.length()];
                rd.read(buf);
                
                return new String(buf);
            } finally {
                if (rd != null) {
                    rd.close();
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't read file '" + file.getAbsolutePath() + "'", ex);
        }

        return null;
    }

    private synchronized ContentFiles getContentFiles(String folderName) {
        ServletContextHelper contextHelper = ServletContextHelper.getInstance();
        ContentFiles contentFiles = contextHelper.getFromContext(getServletContext(), ContentFiles.class, folderName);
        if (contentFiles != null) {
            return contentFiles;
        }

        contentFiles = new ContentFiles();
        contentFiles.load(getServletContext().getRealPath(folderName));

        contextHelper.putToContext(getServletContext(), contentFiles, folderName);
        return contentFiles;
    }
}
