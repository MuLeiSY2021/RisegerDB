package org.riseger.protocol.exception;

public class NullTokenException extends CompileException {

    public NullTokenException() {
        super("Token is null");
    }
}
