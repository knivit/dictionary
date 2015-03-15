package com.tsoft.dict.base;

import com.tsoft.util.file.FileUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Base {
    private Logger logger = Logger.getLogger(Base.class.getName());

    private BaseFileReader fileReader;
    private Index index;
    private StatMap statMap = new StatMap(this);

    protected abstract BaseRecord getRecord(BaseIndex indexRecord);

    public void setFileName(String baseFileName) throws FileNotFoundException, IOException {
        String statFileName = FileUtil.changeFileExt(baseFileName, ".stat");
        logger.log(Level.INFO, "Using an statistic file: " + statFileName);

        getStatMap().load(statFileName);
    }

    public BaseWord getRandomWord() {
        BaseIndex indexRecord = getIndex().getRandomRecord();
        BaseRecord record = getRecord(indexRecord);
        BaseWord word = new BaseWord(indexRecord.getWord(), record.getValue(), indexRecord.getPos());
        return word;
    }

    public BaseWord getWord(long pos) {
        BaseIndex indexRecord = getIndex().getRecord(pos);
        BaseRecord record = getRecord(indexRecord);
        BaseWord word = new BaseWord(indexRecord.getWord(), record.getValue(), indexRecord.getPos());
        return word;
    }

    public Stat getStat(BaseWord word) {
        return getStatMap().get(word);
    }

    public BaseRecord getRecord(String word) {
        BaseIndex indexRecord = index.getRecord(word);
        BaseRecord record = getRecord(indexRecord);
        return record;
    }

    public void close() {
        fileReader.close();
        index.close();
        statMap.close();
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

    private StatMap getStatMap() {
        return statMap;
    }
}
