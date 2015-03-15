package com.tsoft.dict.base.dsl;

import com.tsoft.dict.base.BaseFileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DSLFileReader implements BaseFileReader {
    private Logger logger = Logger.getLogger(DSLFileReader.class.getName());

    private final String EMPTY_LINE = "";

    private RandomAccessFile in;
    private long position;
    private byte[] buf;
    private int bufOffset;
    private int bufLength;
    private char[] str;

    public DSLFileReader(String fileName) throws FileNotFoundException, IOException {
        this(fileName, 8192);
    }

    public DSLFileReader(String fileName, int blockSize) throws FileNotFoundException, IOException {
        in = new RandomAccessFile(fileName, "r");
        buf = new byte[blockSize];
        str = new char[4096];

        setPosition(2);
    }

    @Override
    public void close() {
        try {
            in.close();
       } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't close file", ex);
        }
    }

    public long getPosition() {
        return position;
    }

    @Override
    public void setPosition(long position) throws IOException {
        in.seek(position);
        
        this.position = position;
        bufOffset = bufLength;
    }

    private boolean readBlock() throws IOException {
        bufLength = in.read(buf, 0, buf.length);
        bufLength = (bufLength < 0 ? 0 : bufLength);
        bufOffset = 0;

        return bufLength > 0;
    }

    private final int EOF = -1;

    private int getChar() throws IOException {
        if (bufOffset == bufLength) {
            if (!readBlock()) {
                return EOF;
            }
        }

        position += 2;

        int ch1 = buf[bufOffset ++];
        int ch2 = buf[bufOffset ++];

        // UTF-16LE
        return (char)((ch2 << 8) + (ch1 << 0));
    }

    @Override
    public String readLine() throws IOException {
        while (true) {
            int n = 0;
            boolean eof = false;

            while (n < str.length) {
                int ch = getChar();
                if (ch == EOF) {
                    eof = true;
                    break;
                }

                if (ch == '\r') {
                    // skip '\n'
                    getChar();
                    break;
                }

                str[n ++] = (char)ch;
            }

            if (eof && n == 0) {
                return null;
            }

            // return empty line
            if (n == 0) {
                return EMPTY_LINE;
            }

            String line = new String(str, 0, n);
            return line;
        }
    }
}
