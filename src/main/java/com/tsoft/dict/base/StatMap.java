package com.tsoft.dict.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatMap {
    private Logger logger = Logger.getLogger(StatMap.class.getName());

    private Base base;

    private String fileName;
    private HashMap<BaseWord, Stat> map = new HashMap<BaseWord, Stat>();

    public StatMap(Base base) {
        this.base = base;
    }

    public void load(String fileName) {
        this.fileName = fileName;
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        logger.log(Level.INFO, "Load statistic from file: " + fileName);

        BufferedReader inputStream = null;
        try {
            try {
                inputStream = new BufferedReader(new FileReader(file));

                // parse the columns format
                String line = inputStream.readLine();
                if (line == null) {
                    logger.log(Level.SEVERE, "Can't read statistic, bad file format");
                    return;
                }

                StringTokenizer st = new StringTokenizer(line, ",");
                ArrayList fieldList = Collections.list(st);

                int n = 0;
                String[] mandatoryFields = new String[]{"pos", "showCount", "tipCount"};
                int[] mandatoryFieldIndexes = new int[mandatoryFields.length];
                for (String fieldName : mandatoryFields) {
                    mandatoryFieldIndexes[n] = fieldList.indexOf(fieldName);
                    if (mandatoryFieldIndexes[n] == -1) {
                        logger.log(Level.SEVERE, "Mandatory field pos isn't found, bad file format");
                        return;
                    }

                    n++;
                }

                // process the file
                while ((line = inputStream.readLine()) != null) {
                    st = new StringTokenizer(line, ",");
                    ArrayList valueList = Collections.list(st);

                    String posValue = (String) valueList.get(mandatoryFieldIndexes[0]);
                    long pos = Long.parseLong(posValue);
                    BaseWord word = base.getWord(pos);

                    Stat stat = get(word);

                    String showCountValue = (String) valueList.get(mandatoryFieldIndexes[1]);
                    int showCount = Integer.parseInt(showCountValue);
                    stat.setShowCount(showCount);

                    String tipCountValue = (String) valueList.get(mandatoryFieldIndexes[2]);
                    int tipCount = Integer.parseInt(tipCountValue);
                    stat.setTipCount(tipCount);
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't load statistic", ex);
        }
    }

    public void save() {
        logger.log(Level.INFO, "Save statistic to file: " + fileName);

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter(fileName));

            outputStream.println("pos,showCount,tipCount");
            for (BaseWord word : getMap().keySet()) {
                Stat stat = getMap().get(word);

                outputStream.print(word.getPos());
                outputStream.print(',');
                outputStream.print(stat.getShowCount());
                outputStream.print(',');
                outputStream.print(stat.getTipCount());
                outputStream.println();
            }

            logger.log(Level.INFO, "Statistic is saved");
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "File '" + fileName + "' not found", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't write to '" + fileName + "'", ex);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    public void clear() {
        getMap().clear();
    }

    public Stat get(BaseWord baseWord) {
        Stat stat = getMap().get(baseWord);
        if (stat == null) {
            stat = new Stat();
            getMap().put(baseWord, stat);
        }

        return stat;
    }

    public int size() {
        return getMap().size();
    }

    public void close() {
        save();
        clear();
    }

    private HashMap<BaseWord, Stat> getMap() {
        return map;
    }
}
