package org.riseger.main.compiler.syntax;

import org.riseger.main.compiler.lextcal.Keyword;

public class Syntax {
    private final boolean isKeyword;
    private final Keyword keyword;
    private final int typeCode;

    public Syntax(boolean isKeyword, Keyword keyword, int typeCode) {
        this.isKeyword = isKeyword;
        this.keyword = keyword;
        this.typeCode = typeCode;
    }
}
