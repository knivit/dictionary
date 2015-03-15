package com.tsoft.dict.base;

public class Stat {
    private int showCount;
    private int tipCount;

    public int getShowCount() {
        return showCount;
    }

    public void incShowCount() {
        showCount ++;
    }

    public int getTipCount() {
        return tipCount;
    }

    public void incTipCount() {
        incShowCount();
        tipCount ++;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public void setTipCount(int tipCount) {
        this.tipCount = tipCount;
    }
}
