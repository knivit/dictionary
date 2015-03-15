package com.tsoft.dict.base.dsl;

import com.tsoft.dict.base.Index;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSLIndex extends Index {
    private Logger logger = Logger.getLogger(DSLIndex.class.getName());

    @Override
    protected boolean createIndex(String baseFileName, String indexFileName) {
        logger.log(Level.INFO, "Build index: " + indexFileName);

        DSLFileReader inputStream = null;
        PrintWriter outputStream = null;
        try {
            inputStream = new DSLFileReader(baseFileName);
            outputStream = new PrintWriter(new FileWriter(indexFileName));

            int wordCount = 0;
            while (true) {
                long pos = inputStream.getPosition();

                String line = inputStream.readLine();
                if (line == null) {
                    break;
                }

                if (line.trim().length() == 0 || line.startsWith("#") || line.startsWith("\t")) {
                    continue;
                }

                outputStream.print(line);
                outputStream.print(':');
                outputStream.print(Long.toString(pos));
                outputStream.println();

                wordCount ++;
            }

            logger.log(Level.INFO, Integer.toString(wordCount) + " word(s) processed");
            return true;
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "File '" + baseFileName + "' not found", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't read from '" + indexFileName + "'", ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
            }
        }

        return false;
    }
}
