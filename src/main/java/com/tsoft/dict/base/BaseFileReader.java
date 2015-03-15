package com.tsoft.dict.base;

import java.io.IOException;

public interface BaseFileReader {
    public String readLine() throws IOException;

    public void setPosition(long position) throws IOException;

    public void close();
}
