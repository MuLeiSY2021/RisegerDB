package org.risegerdb.exception;

import org.risegerdb.compile.tokenize.Token;

public class NullTokenException extends CompileException{

    public NullTokenException() {
        super("Token is null");
    }
}
