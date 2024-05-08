package org.riseger.protocol.compiler.function.entity;

import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.StringsFunctional_F;

public class DotStrings_f extends Function_f implements StringsFunctional_F {
    public DotStrings_f() {
        super(DotStrings_f.class);
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
