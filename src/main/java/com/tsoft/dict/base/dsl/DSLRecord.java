package com.tsoft.dict.base.dsl;

import com.tsoft.dict.base.BaseRecord;
import java.util.ArrayList;

public class DSLRecord extends BaseRecord {
    private ArrayList<String> rawLines;
    private ArrayList<String> lines;

    private int pos;

    public DSLRecord(String record) {
        parse(record);
        prepare();
    }

    @Override
    public String getValue() {
        return lines.size() == 0 ? "" : lines.get(0);
    }

    private void prepare() {
        lines = new ArrayList<String>();
        for (String str : rawLines) {
            str = removeBrackets(str);
            str = removeLeadingNumber(str);

            lines.add(str);
        }
    }

    // Remove ( )
    // [m1]vanguard ([com][i][c][p]attr.[/p][/c][/i][/com]) [/m]
    // [m1]tire-cover, (outer) tire [/m]
    // [m1]1. (в [com][i]пр.[/i][/com]) need ([com][i][c][p]d.[/p][/c][/i][/com]),
    //     want ([com][i][c][p]d.[/p][/c][/i][/com]), require ([com][i][c][p]d.[/p][/c][/i][/com]);
    //     ([com][i]в защите, помощи[/i][/com]) stand* in need (of)
    // [/m]
    private String removeBrackets(String str) {
        int n = str.indexOf('(');
        if (n == -1) {
            return str;
        }

        int index = 0;
        StringBuilder buf = new StringBuilder(str);
        while (true) {
            int n1 = buf.indexOf("(", index);
            int n2 = buf.indexOf(")", n1 + 1);
            if (n1 == -1 || n2 == -1) {
                break;
            }

            String token = buf.substring(n1 + 1, n2);
            if (token.indexOf('[') != -1) {
                while (n1 > 0 && buf.charAt(n1 - 1) == ' ') {
                    n1 --;
                }
                buf.delete(n1, n2 + 1);
                index = n1;
            } else {
                index = n2 + 1;
            }
        }

        return buf.toString();
    }

    // Remove leading '1.', '2.' etc
    private String removeLeadingNumber(String str) {
        if (str.length() > 0 && (str.charAt(0) >= '1' && str.charAt(0) <= '9')) {
            int n = str.indexOf('.');
            if (n != -1) {
                return str.substring(n + 1).trim();
            }
        }

        return str;
    }

    private void parse(String record) {
        pos = 0;
        rawLines = new ArrayList<String>();

        int startPos = 0, stopPos = 0;
        boolean isM1 = false;
        boolean isTrn = false;
        while (pos < record.length()) {
            stopPos = pos;
            if (record.charAt(pos) == '[') {
                String token = getToken(record);
                if ("trn".equals(token)) {
                    isTrn = true;
                }

                if ("/trn".equals(token)) {
                    isTrn = false;
                }

                if ("m1".equals(token) && isTrn) {
                    startPos = pos + 1;
                    isM1 = true;
                }

                if ("/m".equals(token) && isM1) {
                    if (stopPos > startPos) {
                        String wordTranslate = record.substring(startPos, stopPos - 1).trim();
                        if (wordTranslate.length() > 0) {
                            rawLines.add(wordTranslate);
                        }
                    }
                    isM1 = false;
                }
            }

            pos ++;
        }
    }

    private String getToken(String record) {
        int startIndex = ++ pos;
        while (pos < record.length()) {
            if (record.charAt(pos) == ']') {
                break;
            }

            pos++;
        }

        String token = record.substring(startIndex, pos);
        return token;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (String str : lines) {
            buf.append(str);
            buf.append('\n');
        }

        return buf.toString();
    }
}
