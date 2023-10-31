package org.riseger.protoctl.compiler.function.loop;

import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.LoopFunctional_f;

public class Back_f extends Function_f implements LoopFunctional_f {
    public Back_f() {
        super(Back_f.class);
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
