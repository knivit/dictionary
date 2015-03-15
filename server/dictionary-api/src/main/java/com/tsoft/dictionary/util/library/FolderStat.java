package com.tsoft.library;

import com.tsoft.dictionary.util.file.DirScanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FolderStat {
    private WordMap folderWords = new WordMap();

    private String path;
    private String folderName;
    
    private int filesCount;
    private int allFilesWordCount;

    private int folderTop;
    private int fileTop;

    public FolderStat(String path, String folderName, int folderTop, int fileTop) {
        this.path = path;
        this.folderName = folderName;
        this.folderTop = folderTop;
        this.fileTop = fileTop;
    }

    public int process(String fileNamesPattern) throws IOException {
        DirScanner files = new DirScanner(path + folderName);
        files.setAddFile(true);
        files.scan(fileNamesPattern);
        ArrayList<String> fileNames = files.getNames();

        int n = 0;
        for (String fileName : fileNames) {
            try {
                FileStat fileStat = new FileStat(path + folderName + fileName, fileTop);
                fileStat.process();
                add(fileStat);

                n ++;
            } catch (Exception ex) {
                System.out.println("Can't process '" + fileName + "'");
                ex.printStackTrace();
            }
        }

        if (getFilesCount() > 0) {
            String outputFileName = path + folderName + folderName + ".info";
            writeTo(outputFileName);
        }

        return n;
    }

    public int getFolderAllWordsCount() {
        return allFilesWordCount;
    }

    public int getFolderUniqueWordsCount() {
        return folderWords.size();
    }

    public int getFilesCount() {
        return filesCount;
    }

    private void writeTo(String outputFileName) throws IOException {
        FileWriter writer = new FileWriter(outputFileName);
        try {
            writer.write("Files," + getFilesCount() + '\n');
            writer.write("Unique," + getFolderUniqueWordsCount() + '\n');

            folderWords.writeTop(writer, folderTop);
        } finally {
            writer.close();
        }
    }

    private void add(FileStat fileStat) throws IOException {
        filesCount ++;

        folderWords.putAllWords(fileStat.getFileWords());
        allFilesWordCount += fileStat.getFileAllWordsCount();
    }
}
