package org.riseger.protoctl.search.function.entity;

import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.StringsFunctional_F;

import java.util.List;

public class Strings_F extends Function_F implements StringsFunctional_F {
    public Strings_F() {
        super(Strings_F.class);
    }

    @Override
    public List<Function_F> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return null;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
