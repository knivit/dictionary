package com.tsoft.dict.base.txt;

import com.tsoft.dict.base.Base;
import com.tsoft.dict.base.BaseRecord;
import com.tsoft.dict.base.BaseIndex;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TXTBase extends Base {
    private Logger logger = Logger.getLogger(TXTBase.class.getName());

    @Override
    public void setFileName(String baseFileName) throws FileNotFoundException, IOException {
        setFileReader(new TXTFileReader(baseFileName));

        setIndex(new TXTIndex());
        getIndex().open(baseFileName);
        
        super.setFileName(baseFileName);
    }

    @Override
    protected BaseRecord getRecord(BaseIndex indexRecord) {
        if (indexRecord.equals(BaseIndex.EMPTY) || !setPosition(indexRecord.getPos())) {
            return BaseRecord.EMPTY;
        }

        try {
            String line = getFileReader().readLine();
            return new TXTRecord(line);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't read record at position " + indexRecord.getPos(), ex);
        }

        return BaseRecord.EMPTY;
    }
}
