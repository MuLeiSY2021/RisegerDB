package org.riseger.protocol.compiler.function.entity;

import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.StringsFunctional_F;

public class Coords_f extends Function_f implements StringsFunctional_F {
    public Coords_f() {
        super(Coords_f.class);
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
