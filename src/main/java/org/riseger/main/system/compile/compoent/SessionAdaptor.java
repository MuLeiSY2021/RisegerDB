package org.riseger.main.system.compile.compoent;

import org.riseger.main.system.compile.lextcal.Lexicator;
import org.riseger.main.system.compile.syntax.Parser;
import org.riseger.main.system.compile.token.Tokenizer;
import org.riseger.protocol.compiler.CommandTree;

public class SessionAdaptor {
    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final Parser parser;

    public SessionAdaptor(Tokenizer tokenizer, Lexicator lexicator, Parser parser) {
        this.tokenizer = tokenizer;
        this.lexicator = lexicator;
        this.parser = parser;
    }

    public SearchSession adapt(String sourceCode, int sessionId) {
        return new SearchSession(sourceCode, tokenizer, lexicator, parser, sessionId);
    }

    public SearchSession adapt(CommandTree commandTree, int sessionId) {
        return new SearchSession(commandTree, tokenizer, lexicator, parser, sessionId);
    }
}
