package com.tsoft.library;

import com.tsoft.dictionary.util.file.DirScanner;
import com.tsoft.dictionary.util.file.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) throws IOException {
        process(new String[] {"-f", "C:\\jprj\\tsoft\\dictionary\\war\\library\\*.txt" });
    }

    private static void process(String[] args) throws IOException {
        if (args == null) {
            usage();
            return;
        }

        String inputFile = null;
        int folderTop = 500;
        int fileTop = 200;

        Iterator<String> arguments = Arrays.asList(args).iterator();
        while (arguments.hasNext()) {
            String arg = arguments.next();
            if ("-f".equals(arg)) {
                inputFile = arguments.next();
                continue;
            }

            if ("-t".equals(arg)) {
                folderTop = Integer.parseInt(arguments.next());
                fileTop = Integer.parseInt(arguments.next());
                continue;
            }

            System.out.println("Unknown arg: " + arg);
            usage();
            return;
        }

        String path = FileUtil.extractFilePath(inputFile);
        String fileNamesPattern = FileUtil.extractFileName(inputFile);

        DirScanner dirs = new DirScanner(path);
        dirs.setAddDirectory(true);
        dirs.scan("*.*");
        ArrayList<String> folderNames = dirs.getNames();

        int n = 0; int f = 0;
        for (String folderName : folderNames) {
            FolderStat stat = new FolderStat(path, folderName, folderTop, fileTop);
            stat.process(fileNamesPattern);

            f ++;
            n += stat.getFilesCount();
        }

        System.out.println("Processed " + f + " folder(s), " + n + " file(s)");
    }

    private static void usage() {
        System.out.print(
            "Usage:\n" +
            "  -f <FileMask> // a mask for the input files\n" +
            "  -t <N1> <N2>  // top N words, N1 for a folder, N2 for a file\n"
        );
    }
}
