package org.riseger.main.compiler.syntax;

import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

import java.util.ListIterator;

public class SyntaxTreeElement implements MultiTreeElement<Syntax> {
    private final ListIterator<SyntaxRule.Type> types;
    private final SyntaxRule syntaxRule;

    public SyntaxTreeElement(ListIterator<SyntaxRule.Type> types, SyntaxRule syntaxRule) {
        this.types = types;
        this.syntaxRule = syntaxRule;
    }

    @Override
    public Equable next(int index) {
        return types.next();
    }

    @Override
    public Syntax get() {
        types.previous();
        return new Syntax(syntaxRule, types.next());
    }

    @Override
    public boolean isTail(int index) {
        return types.hasNext();
    }
}
