package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class LessonResponseTO implements IsSerializable {
    private String dictionaryName;
    private String word;
    private String translatedWord;
    private ArrayList<String> hintList;

    public LessonResponseTO() { }

    public LessonResponseTO(String dictionaryName, String word, String translatedWord, ArrayList<String> hintList) {
        this.dictionaryName = dictionaryName;
        this.word = word;
        this.translatedWord = translatedWord;
        this.hintList = hintList;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public ArrayList<String> getHintList() {
        return hintList;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public String getWord() {
        return word;
    }
}
