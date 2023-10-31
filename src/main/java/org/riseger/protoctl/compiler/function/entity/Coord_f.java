package org.riseger.protoctl.compiler.function.entity;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.COORD_functional;
import org.riseger.protoctl.compiler.function.type.NumberFunctional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class Coord_f extends Function_f implements COORD_functional {
    NumberFunctional number1;

    NumberFunctional number2;

    public Coord_f() {
        super(Coord_f.class);

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
