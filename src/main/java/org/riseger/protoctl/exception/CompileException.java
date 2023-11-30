package org.riseger.protoctl.exception;

import org.riseger.main.system.compile.token.Token;

public class CompileException extends RuntimeException {

    public CompileException(Token token) {
        super("Compile error at \"" + token.getSourceCode() + "\":" + token.getLine() + ":" + token.getColumn());
    }

    public CompileException(String tokenIsNull) {
        super(tokenIsNull);
    }
}
