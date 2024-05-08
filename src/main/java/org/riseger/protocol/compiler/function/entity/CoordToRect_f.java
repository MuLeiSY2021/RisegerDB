package org.riseger.protocol.compiler.function.entity;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.COORD_functional;
import org.riseger.protocol.compiler.function.type.NumberFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class CoordToRect_f extends Function_f implements COORD_functional {
    NumberFunctional number1;

    NumberFunctional number2;

    public CoordToRect_f() {
        super(CoordToRect_f.class);

    }

    public COORD_functional invoke(NumberFunctional number1, NumberFunctional number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.COORD_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
