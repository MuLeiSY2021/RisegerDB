package org.riseger.protoctl.compiler.function.entity;

import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.StringsFunctional_F;

public class TopStrings_f extends Function_f implements StringsFunctional_F {
    public TopStrings_f() {
        super(TopStrings_f.class);
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
