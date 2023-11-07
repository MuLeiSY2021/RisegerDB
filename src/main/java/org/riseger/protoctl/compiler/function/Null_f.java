package org.riseger.protoctl.compiler.function;

import org.riseger.protoctl.compiler.function.type.Functional;
import org.riseger.protoctl.compiler.function.type.UNIVERSAL_functional;

public class Null_f extends Function_f implements UNIVERSAL_functional {
    public Null_f(Class<? extends Functional> clazz) {
        super(Null_f.class);
    }

    @Override
    public Integer getWeight() {
        return null;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
