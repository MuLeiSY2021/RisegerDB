package org.riseger.protocol.compiler.function.number;

import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.Functional;
import org.riseger.protocol.compiler.function.type.NumberFunctional;

public class DivideNumber_f extends Function_f implements NumberFunctional {
    public DivideNumber_f(Class<? extends Functional> clazz) {
        super(clazz);
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
