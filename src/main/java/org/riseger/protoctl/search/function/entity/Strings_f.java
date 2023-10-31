package org.riseger.protoctl.search.function.entity;

import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.StringsFunctional_F;

public class Strings_f extends Function_f implements StringsFunctional_F {
    public Strings_f() {
        super(Strings_f.class);
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
