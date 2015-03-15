package com.tsoft.dictionary.api.txt;

import com.tsoft.dictionary.api.Base;
import com.tsoft.dictionary.api.BaseRecord;
import com.tsoft.dictionary.api.IndexRecord;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TxtBase extends Base {
    private Logger logger = Logger.getLogger(TxtBase.class.getName());

    @Override
    public void setFileName(String baseFileName) throws FileNotFoundException, IOException {
        setFileReader(new TxtBaseFileReader(baseFileName));

        setIndex(new TxtIndex());
        getIndex().open(baseFileName);
    }

    @Override
    protected BaseRecord getRecord(IndexRecord indexRecord) {
        if (indexRecord.equals(IndexRecord.EMPTY) || !setPosition(indexRecord.getPos())) {
            return BaseRecord.EMPTY;
        }

        try {
            String line = getFileReader().readLine();
            return new TxtBaseRecord(line);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't read record at position " + indexRecord.getPos(), ex);
        }

        return BaseRecord.EMPTY;
    }
}
