package com.tsoft.dictionary.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Base {
    private Logger logger = Logger.getLogger(Base.class.getName());

    private BaseFileReader fileReader;
    private Index index;

    protected abstract BaseRecord getRecord(IndexRecord indexRecord);

    public abstract void setFileName(String baseFileName) throws FileNotFoundException, IOException;

    public BaseWord getRandomWord() {
        IndexRecord indexRecord = getIndex().getRandomRecord();
        BaseRecord record = getRecord(indexRecord);
        BaseWord word = new BaseWord(indexRecord.getWord(), record.getValue(), indexRecord.getPos());
        return word;
    }

    public BaseWord getWord(long pos) {
        IndexRecord indexRecord = getIndex().getRecord(pos);
        BaseRecord record = getRecord(indexRecord);
        BaseWord word = new BaseWord(indexRecord.getWord(), record.getValue(), indexRecord.getPos());
        return word;
    }

    public BaseRecord getRecord(String word) {
        IndexRecord indexRecord = index.getRecord(word);
        BaseRecord record = getRecord(indexRecord);
        return record;
    }

    public void close() {
        fileReader.close();
        index.close();
    }

    protected BaseFileReader getFileReader() {
        return fileReader;
    }

    protected void setFileReader(BaseFileReader fileReader) {
        this.fileReader = fileReader;
    }

    protected Index getIndex() {
        return index;
    }

    protected void setIndex(Index index) {
        this.index = index;
    }

    protected boolean setPosition(long pos) {
        try {
            getFileReader().setPosition(pos);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't to set position " + pos, ex);
            return false;
        }

        return true;
    }
}
