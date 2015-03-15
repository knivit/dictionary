package com.tsoft.util.sax;

import java.util.Stack;

public class ElementStack extends Stack {
    public boolean endsWith(String ... strList) {
        if (strList == null || size() < strList.length) {
            return false;
        }

        for (int i = 0; i < strList.length; i++) {
            if (!((String) get(size() - i - 1)).equalsIgnoreCase(strList[i])) {
                return false;
            }
        }
        
        return true;
    }
}
