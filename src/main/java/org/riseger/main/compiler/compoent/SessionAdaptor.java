package org.riseger.main.compiler.compoent;

import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.token.Tokenizer;

public class SessionAdaptor {
    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final Parser parser;

    public SessionAdaptor(Tokenizer tokenizer, Lexicator lexicator, Parser parser) {
        this.tokenizer = tokenizer;
        this.lexicator = lexicator;
        this.parser = parser;
    }

    public SearchSession adapt(String sourceCode) {
        return new SearchSession(sourceCode, tokenizer, lexicator, parser);
    }
}
