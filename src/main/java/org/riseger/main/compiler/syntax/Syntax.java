package org.riseger.main.compiler.syntax;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.utils.tree.Equable;

import java.util.Objects;

@Getter
public class Syntax implements Equable {
    private final boolean isKeyword;
    private final int hashCode;

    @Setter
    private Class<Function_f> functionFClass;

    public Syntax(SyntaxRule syntaxRule, SyntaxRule.Tile tile) {
        if (tile == null) {
            this.isKeyword = false;
            this.hashCode = -1;
        } else {
            this.isKeyword = tile.isKey();
            if (this.isKeyword) {
                this.hashCode = Keyword.addKeyword(tile.getValue());
            } else {
                this.hashCode = syntaxRule.getRuleMap().get(tile.getValue()).getTypeId();
            }
        }
    }

    public boolean equals(Token token) {
        return this.isKeyword &&
                token.getType().equals(TokenType.KEYWORD) &&
                this.hashCode == token.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Syntax syntax = (Syntax) o;
        return isKeyword == syntax.isKeyword && hashCode == syntax.hashCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isKeyword, hashCode);
    }
}
