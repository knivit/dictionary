package com.tsoft.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * The DirScanner class provides the functionality of recursive scanning thru directories,
 * using regular expressions (wild-cards) patterns. Here are some examples:
 *
 * DirScanner.getFiles(new File(System.getProperty("user.dir")), "*.java", "-*\swing\*") - (non-recursively)
 *    Find all files that have a ".java" suffix excluding files whose one of their parent directories is named "swing"
 *
 * DirScanner.getFilesRecursive(new File(System.getProperty("user.dir")), "*.java") - (recursively)
 *    Find all files that have a ".java" suffix in the "user.dir" directory.
 *
 * DirScanner.getFilesRecursive(new File(System.getProperty("user.dir")), "*\swing\*.java") - (recursively)
 *    Find all files that have a ".java" suffix and one of their parent directories is named "swing".
*/
public class DirScanner {
    private static final String SEP = System.getProperty("file.separator");
    private final List<File> files = new ArrayList<File>();
    private final List<String> patts = new ArrayList<String>();

    public static List<File> getFiles(File dir, String... patterns) {
        return scan(dir, false, patterns);
    }

    public static List<String> getFileNames(File dir, String... patterns) {
        return getNames(scan(dir, false, patterns), dir.isAbsolute());
    }

    public static List<File> getFilesRecursive(File dir, String... patterns) {
        return scan(dir, true, patterns);
    }

    public static List<String> getFileNamesRecursive(File dir, String... patterns) {
        return getNames(scan(dir, true, patterns), dir.isAbsolute());
    }

    private static ArrayList<String> getNames(List<File> files, boolean isAbsolute) {
        ArrayList<String> names = new ArrayList<String>();
        for (File file : files) {
            if (isAbsolute) {
                names.add(file.getAbsolutePath());
            } else {
                names.add(file.getPath().substring(1));
            }
        }
        return names;
    }

    private static boolean isSubtract(String patt) {
        return patt.startsWith("-");
    }

    private static String rawPatt(String patt) {
        if (!isSubtract(patt)) {
            return patt;
        }
        return patt.substring(1);
    }

    public static List<File> scan(File dir, boolean isRecursive, String... patterns) {
        DirScanner s = new DirScanner();
        for (String p : patterns) {
            p = p.replace(SEP, "/");
            p = p.replace(".", "\\.");
            p = p.replace("*", ".*");
            p = p.replace("?", ".?");
            s.patts.add(p);
        }

        s.scan(dir, new File("/"), isRecursive);
        return s.files;
    }

    private void scan(File dir, File path, boolean isRecursive) {
        File[] fs = dir.listFiles();
        for (File f : fs) {
            File rel = new File(path, f.getName());
            if (f.isDirectory() && isRecursive) {
                scan(f, rel, true);
                continue;
            }

            if (match(patts, rel)) {
                files.add(rel);
            }
        }
    }

    private static boolean match(Iterable<String> patts, File rel) {
        boolean ok = false;
        for (String p : patts) {
            boolean subtract = isSubtract(p);
            p = rawPatt(p);

            boolean b = match(p, rel);
            if (b && subtract) {
                return false;
            }

            if (b) {
                ok = true;
            }
        }

        return ok;
    }

    private static boolean match(String p, File rel) {
        String s = rel.getName();
        if (p.indexOf('/') >= 0) {
            s = rel.toString();
        }

        s = s.replace(SEP, "/");
        return s.matches(p);
    }
}
