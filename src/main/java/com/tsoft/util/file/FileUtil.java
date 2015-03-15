package com.tsoft.util.file;

public final class FileUtil {
    private FileUtil() { }

    /**
     * Example:
     *     String fileName = FileUtil.changeFileExt("hello.world", ".all"); // "hello.all"
     */
    public static String changeFileExt(String fileName, String  ext) {
        int n = fileName.lastIndexOf('.');
        String newFileName;
        if (n == -1) {
            newFileName = fileName + ext;
        } else {
            newFileName = fileName.substring(0, n) + ext;
        }
        return newFileName;
    }
}
