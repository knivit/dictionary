package com.tsoft.dictionary.api;

import java.io.IOException;

public interface BaseFileReader {
    public String readLine() throws IOException;

    public void setPosition(long position) throws IOException;

    public void close();
}
