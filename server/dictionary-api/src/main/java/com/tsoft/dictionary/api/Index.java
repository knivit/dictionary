package com.tsoft.dictionary.api;

import com.tsoft.dictionary.util.file.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Index {
    private static final Logger logger = Logger.getLogger(Index.class.getName());

    private final Random random = new Random();
    private ArrayList<IndexRecord> index = new ArrayList<IndexRecord>();

    protected abstract boolean createIndex(String baseFileName, String indexFileName);

    public void open(String baseFileName) throws FileNotFoundException, IOException {
        reset();

        String indexFileName = FileUtil.changeFileExt(baseFileName, ".idx");
        logger.log(Level.INFO, "Using an index file: " + indexFileName);

        // create the index file
        File indexFile = new File(indexFileName);
        if (!indexFile.exists()) {
            if (!createIndex(baseFileName, indexFileName)) {
                throw new IOException("Can't create an index file");
            }
            logger.log(Level.INFO, "Index file was created");
        }

        // read the index into memory
        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(indexFile));

            String line;
            while ((line = inputStream.readLine()) != null) {
                int n = line.indexOf(':');
                String word = line.substring(0, n);
                long pos = Long.parseLong(line.substring(n + 1));

                IndexRecord record = new IndexRecord(word, pos);
                index.add(record);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public int getSize() {
        return index.size();
    }

    public IndexRecord getRecord(String word) {
        for (IndexRecord record : index) {
            if (record.getWord().equalsIgnoreCase(word)) {
                return record;
            }
        }

        return IndexRecord.EMPTY;
    }

    public IndexRecord getRecord(long pos) {
        for (IndexRecord record : index) {
            if (record.getPos() == pos) {
                return record;
            }
        }

        return IndexRecord.EMPTY;
    }

    public IndexRecord getRandomRecord() {
        int n = random.nextInt(getSize());
        return index.get(n);
    }

    public void close() {
        reset();
    }

    private void reset() {
        index.clear();
        logger.log(Level.INFO, "Index destroyed");
    }
}
