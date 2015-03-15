package com.tsoft.dictionary.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class FileUtil {
    private FileUtil() { }

    /**
     * Example:
     * String fileName = FileUtil.changeFileExt("hello.world", ".all"); // "hello.all"
     */
    public static String changeFileExt(String fileName, String  ext) {
        if (fileName == null) {
            return null;
        }

        String newFileName;
        int n = fileName.lastIndexOf('.');
        if (n == -1) {
            newFileName = fileName + ext;
        } else {
            newFileName = fileName.substring(0, n) + ext;
        }
        return newFileName;
    }

    public static String extractFileName(String fileName) {
        if (fileName == null) {
            return null;
        }

        int n = fileName.lastIndexOf(File.separator);
        if (n == -1) {
            return fileName;
        }

        return fileName.substring(n + 1);
    }

    public static String extractFilePath(String fileName) {
        if (fileName == null) {
            return null;
        }

        int n = fileName.lastIndexOf(File.separator);
        if (n == -1) {
            return ".";
        }

        return fileName.substring(0, n);
    }

    public static String removeFileExt(String fileName) {
        if (fileName == null) {
            return null;
        }

        int n = fileName.lastIndexOf('.');
        if (n == -1) {
            return fileName;
        }

        return fileName.substring(0, n);
    }

    public static HashMap<String, String> getFilesMap(ArrayList<String> fileList) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String fileName : fileList) {
            File file = new File(fileName);
            map.put(file.getName(), fileName);
        }
        return map;
    }
}
