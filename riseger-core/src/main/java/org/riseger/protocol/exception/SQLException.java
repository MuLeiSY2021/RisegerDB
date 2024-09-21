package org.riseger.protocol.exception;

public class SQLException extends InterruptedException {
    public SQLException() {
    }

    public SQLException(String s) {
        super(s);
    }
}
