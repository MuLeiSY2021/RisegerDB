package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.COORD_functional;
import org.riseger.protoctl.search.function.type.NUMBER_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class Coord_f extends Function_f implements COORD_functional {
    NUMBER_functional number1;

    NUMBER_functional number2;

    public Coord_f() {
        super(Coord_f.class);

    }

    public COORD_functional invoke(NUMBER_functional number1, NUMBER_functional number2) {
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
