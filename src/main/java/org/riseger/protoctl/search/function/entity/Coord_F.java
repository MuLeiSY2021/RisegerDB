package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class Coord_F extends Function_F implements COORD_FUNCTIONAL {
    NUMBER_FUNCTIONAL number1;

    NUMBER_FUNCTIONAL number2;

    public Coord_F() {
        super(Coord_F.class);

    }

    public COORD_FUNCTIONAL invoke(NUMBER_FUNCTIONAL number1, NUMBER_FUNCTIONAL number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.COORD_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
