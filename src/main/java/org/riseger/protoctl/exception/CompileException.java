package org.riseger.protoctl.exception;

import org.riseger.main.api.compile.tokenize.Token;

public class CompileException extends RuntimeException {

    public CompileException(Token token) {
        super("Compile error at \"" + token.getSourceCode() + "\":" + token.getLine() + ":" + token.getColumn());
    }

    public CompileException(String tokenIsNull) {
        super(tokenIsNull);
    }
}
