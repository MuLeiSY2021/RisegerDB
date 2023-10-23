package org.riseger.main.compiler.syntax;

import org.riseger.protoctl.search.function.Function_f;
import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

import java.util.ListIterator;

public class SyntaxTreeElement implements MultiTreeElement<Syntax> {
    private final ListIterator<SyntaxRule.Type> types;
    private final SyntaxRule syntaxRule;

    private final Class<? extends Function_f> functionClazz;

    public SyntaxTreeElement(ListIterator<SyntaxRule.Type> types, SyntaxRule syntaxRule, Class<? extends Function_f> functionClazz) {
        this.types = types;
        this.syntaxRule = syntaxRule;
        this.functionClazz = functionClazz;
    }

    @Override
    public Equable next(int index) {
        return types.next();
    }

    @Override
    public Syntax get() {
        types.previous();
        return new Syntax(syntaxRule, types.next(), functionClazz);
    }

    @Override
    public boolean isTail(int index) {
        return types.hasNext();
    }
}
