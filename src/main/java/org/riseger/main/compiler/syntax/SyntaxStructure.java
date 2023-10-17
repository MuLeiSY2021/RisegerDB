package org.riseger.main.compiler.syntax;

import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;

public class SyntaxStructure {
    private final Token token;

    private final boolean isKeyword;

    private final Keyword keyword;

    private final int typeCode;

    public SyntaxStructure(Token token, boolean isKeyword, Keyword keyword, int typeCode) {
        this.token = token;
        this.isKeyword = isKeyword;
        this.keyword = keyword;
        this.typeCode = typeCode;
    }
}
