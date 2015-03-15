package com.tsoft.dict.base.dsl;

import com.tsoft.dict.base.Base;
import com.tsoft.dict.base.BaseRecord;
import com.tsoft.dict.base.BaseIndex;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSLBase extends Base {
    private Logger logger = Logger.getLogger(DSLBase.class.getName());

    @Override
    public void setFileName(String baseFileName) throws FileNotFoundException, IOException {
        setFileReader(new DSLFileReader(baseFileName));
        
        setIndex(new DSLIndex());
        getIndex().open(baseFileName);

        super.setFileName(baseFileName);
    }

    @Override
    protected BaseRecord getRecord(BaseIndex indexRecord) {
        if (indexRecord.equals(BaseIndex.EMPTY) || !setPosition(indexRecord.getPos())) {
            return BaseRecord.EMPTY;
        }

        try {
            String line;
            boolean isFirst = true;
            StringBuilder buf = new StringBuilder();
            while ((line = getFileReader().readLine()) != null) {
                if (line.trim().length() == 0 || line.startsWith("#")) {
                    break;
                }

                if (isFirst) {
                    isFirst = false;
                } else {
                    buf.append(line.trim());
                }
            }

            String str = buf.toString();
            return new DSLRecord(str);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't read record at position " + indexRecord.getPos(), ex);
        }

        return BaseRecord.EMPTY;
    }
}
