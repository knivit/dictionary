package com.tsoft.dictionary.api.txt;

import com.tsoft.dictionary.api.BaseRecord;

public class TxtBaseRecord extends BaseRecord {
    private String value;

    public TxtBaseRecord(String record) {
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
