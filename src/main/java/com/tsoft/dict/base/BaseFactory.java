package com.tsoft.dict.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseFactory {
    private static Logger logger = Logger.getLogger(BaseFactory.class.getName());

    private BaseFactory() { }

    public static Base getBase(String baseFileName) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            FileNotFoundException, IOException {
        int n = baseFileName.lastIndexOf('.');
        if (n == -1) {
            throw new IOException("Unknown dictionary file: " + baseFileName);
        }

        String fileExt = baseFileName.substring(n + 1);
        String baseClassName = BaseFactory.class.getPackage().getName() + '.' + fileExt.toLowerCase() + '.' + fileExt.toUpperCase() + "Base";
        logger.log(Level.INFO, "Using base: " + baseClassName);

        Class baseClass = Class.forName(baseClassName);
        Base base = (Base)baseClass.newInstance();

        base.setFileName(baseFileName);
        
        return base;
    }
}
