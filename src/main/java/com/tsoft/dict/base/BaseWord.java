package com.tsoft.dict.base;

public class BaseWord extends BaseIndex {
    private String value;

    public BaseWord(String word, String value, long pos) {
        super(word, pos);
        this.value = value;
    }

    public BaseWord(BaseWord baseWord) {
        this(baseWord.getWord(), baseWord.getValue(), baseWord.getPos());
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
