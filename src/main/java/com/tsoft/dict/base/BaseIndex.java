package com.tsoft.dict.base;

public class BaseIndex {
    public static final BaseIndex EMPTY = new BaseIndex(null, -1L);

    private String word;
    private long pos;

    public BaseIndex(String word, long pos) {
        this.word = word;
        this.pos = pos;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseIndex other = (BaseIndex) obj;
        if (this.pos != other.pos) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.pos ^ (this.pos >>> 32));
        return hash;
    }
}
