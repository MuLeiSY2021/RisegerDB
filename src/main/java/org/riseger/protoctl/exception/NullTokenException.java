package org.riseger.protoctl.exception;

public class NullTokenException extends CompileException {

    public NullTokenException() {
        super("Token is null");
    }
}
