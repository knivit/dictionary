package com.tsoft.dictionary.util.file;

import java.io.File;
import java.util.ArrayList;

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
    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<String> patts = new ArrayList<String>();

    private File dir;
    private boolean isRecursive;
    private boolean isAddDirectory;
    private boolean isAddFile;

    public DirScanner(String dirName) {
        this(new File(dirName));
    }

    public DirScanner(File dir) {
        this.dir = dir;
    }

    public int scan(String ... patterns) {
        patts.clear();
        for (String p : patterns) {
            p = p.replace(File.separator, "/");
            p = p.replace(".", "\\.");
            p = p.replace("*", ".*");
            p = p.replace("?", ".?");
            patts.add(p);
        }

        files.clear();
        scan(dir, new File("/"));

        return files.size();
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<String>(files.size());
        for (File file : files) {
            names.add(file.getPath());
        }
        return names;
    }

    public ArrayList<File> getFiles() {
        ArrayList<File> fs = new ArrayList<File>(files);
        return fs;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean isRecursive) {
        this.isRecursive = isRecursive;
    }

    public boolean isAddDirectory() {
        return isAddDirectory;
    }

    public void setAddDirectory(boolean isAddDirectory) {
        this.isAddDirectory = isAddDirectory;
    }

    public boolean isAddFile() {
        return isAddFile;
    }

    public void setAddFile(boolean isAddFile) {
        this.isAddFile = isAddFile;
    }

    private void scan(File parentDir, File path) {
        File[] fs = parentDir.listFiles();
        if (fs == null) {
            return;
        }

        for (File f : fs) {
            File rel = new File(path, f.getName());
            if (f.isDirectory() && isRecursive()) {
                scan(f, rel);
            }

            if (match(patts, rel)) {
                if (isAddDirectory() && f.isDirectory()) {
                    files.add(rel);
                }

                if (isAddFile() && f.isFile()) {
                    files.add(rel);
                }
            }
        }
    }

    private boolean isSubtract(String patt) {
        return patt.startsWith("-");
    }

    private String rawPatt(String patt) {
        if (!isSubtract(patt)) {
            return patt;
        }

        return patt.substring(1);
    }

    private boolean match(Iterable<String> patts, File rel) {
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

    private boolean match(String p, File rel) {
        String s = rel.getName();
        if (p.indexOf('/') >= 0) {
            s = rel.toString();
        }

        s = s.replace(File.separator, "/");
        return s.matches(p);
    }
}
