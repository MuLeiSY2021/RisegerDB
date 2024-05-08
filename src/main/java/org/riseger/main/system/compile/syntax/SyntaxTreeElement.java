package org.riseger.main.system.compile.syntax;

import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

public class SyntaxTreeElement implements MultiTreeElement<Class<Function_f>> {
    private final SyntaxRule.Meta meta;

    private final SyntaxRule rule;

    public SyntaxTreeElement(SyntaxRule.Meta meta, SyntaxRule rule) {
        this.meta = meta;
        this.rule = rule;
    }

    @Override
    public Equable next(int index) {
        return new Syntax(rule, meta.getTiles().get(index));
    }

    @Override
    public Class<Function_f> get() {
        return meta.getFunctionClazz();
    }

    @Override
    public boolean isTail(int index) {
        return meta.getTiles().size() == index + 1;
    }
}
