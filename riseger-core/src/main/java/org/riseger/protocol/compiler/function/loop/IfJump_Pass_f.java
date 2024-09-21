package org.riseger.protocol.compiler.function.loop;

import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.LoopFunctional_f;

public class IfJump_Pass_f extends Function_f implements LoopFunctional_f {
    public IfJump_Pass_f() {
        super(IfJump_Pass_f.class);
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
