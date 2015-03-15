package com.tsoft.library;

import com.tsoft.dictionary.util.file.FileUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FileStat {
    private WordMap fileWords = new WordMap();
    private int fileWordCount;

    private String inputFileName;
    private int fileTop;

    private static List nonWordList = Arrays.asList(new String[] {
        "a", "about", "again", "after", "all", "also", "am", "an", "and", "another", "any", "are", "as", "at",
        "back", "be", "been", "before", "but", "by",
        "came", "can", "can t", "cannot", "chapter", "could",
        "did", "didn t", "do", "don t", "down", "dr",
        "for", "from",
        "had", "have", "has", "he", "her", "here", "him", "his", "how",
        "i", "i d", "i m", "i ve",  "if", "i ll", "in", "into", "is", "it", "its", "it s",
        "just",
        "get", "go", "got", "going",
        "like",
        "me", "more", "mr", "must", "my",
        "no", "not", "now",
        "of", "off", "on", "one", "or", "other", "over", "out",
        "said", "same", "see", "seen", "she", "she s", "so", "still",
        "take", "than", "that", "that s", "the", "then", "their", "them", "there", "there s", "they", "this", "those",
        "through", "to", "told", "too", "took",
        "up", "us",
        "was", "wasn t", "went", "what", "when", "we", "we re", "were", "which", "who", "will", "with", "would",
        "yet", "you", "your", "you re", "you ve"
    });

    public FileStat(String inputFileName, int fileTop) {
        this.inputFileName = inputFileName;
        this.fileTop = fileTop;
    }

    public void process() throws FileNotFoundException, IOException {
        File inputFile = new File(inputFileName);
        if (!inputFile.exists()) {
            System.out.println("File " + inputFile.getAbsolutePath() + " not found");
            return;
        }

        System.out.print("Process file: " + inputFile.getAbsolutePath() + " ... ");
        buildWordMap(inputFile);
        writeTop();

        System.out.println("DONE");
    }

    private void buildWordMap(File inputFile) throws FileNotFoundException {
        fileWordCount = 0;
        Scanner in = new Scanner(inputFile);
        try {
            while (in.hasNext()) {
                String word = in.next();
                
                String normalizedWord = normalize(word);
                if (notWord(normalizedWord)) {
                    continue;
                }

                fileWords.putWord(normalizedWord);
                fileWordCount ++;
            }
        } finally {
            in.close();
        }
    }

    public WordMap getFileWords() {
        return fileWords;
    }

    public int getFileAllWordsCount() {
        return fileWordCount;
    }

    public int getFileUniqueWordsCount() {
        return fileWords.size();
    }

    private void writeTop() throws IOException {
        String outputFileName = FileUtil.changeFileExt(inputFileName, ".info");

        FileWriter writer = new FileWriter(outputFileName);
        try {
            writer.write("Words," + getFileAllWordsCount() + '\n');
            int uqPercent = getFileAllWordsCount() == 0 ? 100 : (getFileWords().size() * 100 / getFileAllWordsCount());
            writer.write("Unique," + getFileWords().size() + " (" + uqPercent + "%)\n");
            
            getFileWords().writeTop(writer, fileTop);
        } finally {
            writer.close();
        }
    }

    private boolean notWord(String word) {
        return word.length() == 0 || nonWordList.contains(word);
    }

    private String normalize(String word) {
        StringBuilder buf = new StringBuilder(word.toLowerCase());
        for (int i = 0; i < buf.length(); i ++) {
            char ch = buf.charAt(i);
            if (ch == '<' || ch == '>' || ch == '(' || ch == ')' ||
                ch == '\'' || ch == '.' || ch == '?' || ch == ',' ||
                ch =='"' || ch == ':' || ch == '!' || ch == '-' ||
                ch =='=' || ch == '*') {
                buf.setCharAt(i, ' ');
            }
        }

        return buf.toString().trim();
    }
}
