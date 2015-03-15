package com.tsoft.dictionary.api;

public class IndexRecord {
    public static final IndexRecord EMPTY = new IndexRecord(null, -1L);

    private String word;
    private long pos;

    public IndexRecord(String word, long pos) {
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
        final IndexRecord other = (IndexRecord) obj;
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
