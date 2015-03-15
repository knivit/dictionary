package com.tsoft.library;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordMap {
    private HashMap<String, WordCounter>  map = new HashMap<String, WordCounter>();

    public void putWord(String word) {
        putWord(word, 1);
    }

    private void putWord(String word, int n) {
        WordCounter count = map.get(word);
        if (count == null) {
            count = new WordCounter();
            map.put(word, count);
        }
        count.inc(n);
    }

    public void putAllWords(WordMap newMap) {
        Iterator<Map.Entry<String, WordCounter>> it = newMap.map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, WordCounter> entry = it.next();
            putWord(entry.getKey(), entry.getValue().getCount());
        }
    }

    public int size() {
        return map.size();
    }

    public ArrayList<Map.Entry<String, WordCounter>> getSortedList() {
        ArrayList<Map.Entry<String, WordCounter>> list = new ArrayList<Map.Entry<String, WordCounter>>(map.entrySet());

        // Sort the list using an annonymous inner class implementing Comparator for the compare method
        Collections.sort(list, new Comparator<Map.Entry<String, WordCounter>>(){
            @Override
            public int compare(Map.Entry<String, WordCounter> entry1, Map.Entry<String, WordCounter> entry2) {
                int n1 = entry1.getValue().getCount();
                int n2 = entry2.getValue().getCount();
                return (n1 == n2 ? 0 : (n1 > n2 ? -1 : 1));
            }
        });

        return list;
    }

    public void writeTop(FileWriter writer, int top) throws IOException {
        // sort the word map by the word occuriences
        ArrayList<Map.Entry<String, WordCounter>> list = getSortedList();

        writer.write("Top," + top + '\n');
        Iterator<Map.Entry<String, WordCounter>> it = list.iterator();
        while (it.hasNext() && top > 0) {
            Map.Entry<String, WordCounter> entry = it.next();
            writer.write(entry.getKey() + ',' + entry.getValue().getCount() + '\n');
            top --;
        }
    }
}
