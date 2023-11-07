package org.riseger.main.compiler.syntax;

import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

public class SyntaxTreeElement implements MultiTreeElement<Class<Function_f>> {
    private final SyntaxRule.Meta meta;

    public SyntaxTreeElement(SyntaxRule.Meta meta) {
        this.meta = meta;
    }

    @Override
    public Equable next(int index) {
        return meta.getTiles().get(index);
    }

    @Override
    public Class<Function_f> get() {
        return meta.getFunctionClazz();
    }

    @Override
    public boolean isTail(int index) {
        return meta.getTiles().size() == index;
    }
}
