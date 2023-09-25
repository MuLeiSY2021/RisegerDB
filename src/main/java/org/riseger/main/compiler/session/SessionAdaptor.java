package org.riseger.main.compiler.session;

import org.riseger.main.compiler.lextcal.KeywordsTree;
import org.riseger.main.compiler.lextcal.Tokenizer;
import org.riseger.main.compiler.syntax.SyntaxTree;

public class SessionAdaptor {
    Tokenizer tokenizer;

    KeywordsTree keywordsTree;

    SyntaxTree syntaxTree;

    public SessionAdaptor(Tokenizer tokenizer, SyntaxTree syntaxTree) {
        this.tokenizer = tokenizer;
        this.syntaxTree = syntaxTree;
    }

    public Session adapt(String sourceCode) {
        return new Session(sourceCode, tokenizer, syntaxTree);
    }
}
