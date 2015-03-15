package com.tsoft.dictionary.server.util;

import com.tsoft.dictionary.server.app.GWTClient;
import java.util.ArrayList;

@GWTClient
public class StringHelper {
    private StringHelper() { }

    /**
     * @return true if str is null or empty string
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static int getCharCount(String str, char ch) {
        int count = 0;
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i ++) {
                if (str.charAt(i) == ch) {
                    count ++;
                }
            }
        }

        return count;
    }

    /**
     * @param str a string like <a href="<value>" />
     * @return <value>
     */
    public static String getHref(String str) {
        if (!isEmpty(str)) {
            int pos = str.toLowerCase().indexOf("href");
            if (pos != -1) {
                char ch = 0;
                int n1 = 0, n2 = 0;
                for (int i = pos + 4; i < str.length() - 1; i ++) {
                    if (n1 == 0) {
                        if (str.charAt(i) == '"' || str.charAt(i) == '\'') {
                            ch = str.charAt(i);
                            n1 = i + 1;
                            continue;
                        }
                    } else {
                        if (str.charAt(i) == ch) {
                            n2 = i - 1;
                            break;
                        }
                    }
                }

                if (n2 - n1 > 0) {
                    return str.substring(n1, n2 + 1);
                }
            }
        }

        return null;
    }

    public static String getPatternValue(String pattern) {
        ArrayList<String> tokenList = new ArrayList<String>();
        if (isEmpty(pattern)) {
            return "%";
        }

        StringBuilder token = new StringBuilder();
        boolean inToken = false;
        for (int i = 0; i < pattern.length(); i ++) {
            char ch = pattern.charAt(i);
            if (ch == '"') {
                if (inToken) {
                    if (token.length() > 0) {
                        tokenList.add(token.toString());
                        token.setLength(0);
                    }
                    inToken = false;
                    continue;
                }

                if (token.length() > 0) {
                    tokenList.add(token.toString().toLowerCase());
                    token.setLength(0);
                }
                inToken = true;
                continue;
            }

            if (inToken) {
                token.append(ch);
                continue;
            }

            if (ch == ' ') {
                if (token.length() > 0) {
                    tokenList.add(token.toString().toLowerCase());
                    token.setLength(0);
                }
            } else {
                token.append(ch);
            }
        }

        if (token.length() > 0) {
            tokenList.add(token.toString().toLowerCase());
        }
        
        StringBuilder buf = new StringBuilder(pattern.length());
        for (String str : tokenList) {
            buf.append(str).append("%");
        }

        if (buf.length() == 0) {
            return "%";
        }

        // Datanucleus problem here:
        //    Wildcard must appear at the end of the expression string (only prefix matches are supported)
        // buf.append("%");

        return buf.toString();
    }
}
