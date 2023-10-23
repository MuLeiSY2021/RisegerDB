package org.riseger.main.compiler.syntax;

import lombok.Getter;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.protoctl.search.function.Function_f;

@Getter
public class Syntax {
    private final boolean isKeyword;
    private final Keyword keyword;
    private final int typeCode;

    private final Class<? extends Function_f> function;

    public Syntax(SyntaxRule syntaxRule, SyntaxRule.Type type, Class<? extends Function_f> functionClazz) {
        if (type == null) {
            this.isKeyword = false;
            this.typeCode = -2;
            this.keyword = null;
            this.function = null;
        } else {
            this.isKeyword = type.isKey();
            if (this.isKeyword) {
                this.keyword = Keyword.addKeyword(type.getValue());
                this.typeCode = -1;
            } else {
                this.typeCode = syntaxRule.getRuleMap().get(type.getValue()).getTypeId();
                this.keyword = null;
            }
            this.function = functionClazz;
        }
    }

    public boolean equals(Token token) {
        return this.isKeyword &&
                token.getType().equals(TokenType.KEYWORD) &&
                this.typeCode == token.getId();
    }


}
