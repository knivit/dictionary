package com.tsoft.dict.base.txt;

import com.tsoft.dict.base.BaseRecord;

public class TXTRecord extends BaseRecord {
    private String value;

    public TXTRecord(String record) {
        int n1 = record.indexOf('|');
        int n2 = record.indexOf('|', n1 + 1);
        if (n2 > n1) {
            value = record.substring(n1 + 1, n2);
        } else {
            value = record.substring(n1 + 1);
        }

        value = value.replace(';', '\n');
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

