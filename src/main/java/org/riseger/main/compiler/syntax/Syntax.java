package org.riseger.main.compiler.syntax;

import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.utils.tree.Equable;

import java.util.Objects;

@Getter
public class Syntax implements Equable {
    private final boolean isKeyword;
    private final int id;

    private static final Logger LOG = Logger.getLogger(Syntax.class);

    private String symbol = "null";

    @Setter
    private Class<Function_f> functionFClass;

    public Syntax(SyntaxRule syntaxRule, SyntaxRule.Tile tile) {
        int hashCode1;
        if (tile == null) {
            this.isKeyword = false;
            hashCode1 = -1;
        } else {
            this.symbol = tile.getValue();
            this.isKeyword = tile.isKey();
            if (this.isKeyword) {
                hashCode1 = Keyword.addKeyword(tile.getValue());
            } else {
                try {
                    hashCode1 = syntaxRule.getRuleMap().get(tile.getValue()).getTypeId();
                } catch (Exception e) {
                    LOG.error("Failed to decode " + tile.getValue(), e);
                    hashCode1 = -1;
                }
            }
        }
        this.id = hashCode1;
    }

    public boolean equals(Token token) {
        return this.isKeyword &&
                token.getType().equals(TokenType.KEYWORD) &&
                this.id == token.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Syntax syntax = (Syntax) o;
        return isKeyword == syntax.isKeyword && id == syntax.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isKeyword, id);
    }

    @Override
    public String toString() {
        return '\'' + symbol + "',";
    }
}
