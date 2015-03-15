package com.tsoft.config;

import com.tsoft.mvc.Model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigModel extends Model {
    private Logger logger = Logger.getLogger(ConfigModel.class.getName());

    private String fileName;
    private Reader reader;

    private ArrayList<ConfigSection> sectionList = new ArrayList<ConfigSection>();

    @Override
    public void init() throws Exception {
        sectionList.clear();

        BufferedReader bufferedReader = null;
        try {
            if (reader == null) {
                reader = new FileReader(fileName);
            }

            bufferedReader = new BufferedReader(reader);

            String line;
            ConfigSection section = null;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                // section starts
                if (line.startsWith("[") && line.endsWith("]")) {
                    String sectionName = line.substring(1, line.length() - 1);
                    section = getSection(sectionName);
                    continue;
                }

                if (section == null) {
                    section = new ConfigSection("");
                    sectionList.add(section);
                }

                section.addProperty(line);
            }

            logger.log(Level.INFO, "Config '" + fileName + "' was loaded");
        } catch (Exception ex) {
            logger.log(Level.INFO, "Can't read config '" + fileName + "'");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    public ConfigSection getSection(String name) {
        ConfigSection section = findSection(name);
        if (section == null) {
            section = new ConfigSection(name);
            sectionList.add(section);
        }

        return section;
    }

    public ConfigSection findSection(String name) {
        for (ConfigSection section : sectionList) {
            if (section.getName().equalsIgnoreCase(name)) {
                return section;
            }
        }

        return null;
    }

    @Override
    public void close() {
        if (isChanged()) {
            save();
        }

        logger.log(Level.INFO, "Config '" + fileName + "' was closed");
    }

    private boolean isChanged() {
        for (ConfigSection section : sectionList) {
            if (section.isChanged()) {
                return true;
            }
        }

        return false;
    }

    private void save() {
        // don't write to the read-only config
        if (fileName == null) {
            return;
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));

            for (ConfigSection section : sectionList) {
                writer.write('[' + section.getName() + "]\n");
                section.save(writer);
            }

            logger.log(Level.INFO, "Config '" + fileName + "' was saved");
         } catch (Exception ex) {
            logger.log(Level.INFO, "Can't write config '" + fileName + "'", ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
   }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }
}
