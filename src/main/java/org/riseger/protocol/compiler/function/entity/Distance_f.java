package org.riseger.protocol.compiler.function.entity;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.COORD_functional;
import org.riseger.protocol.compiler.function.type.NumberFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Distance_f extends Function_f implements NumberFunctional {
    private COORD_functional coord1;

    private COORD_functional coord2;

    public Distance_f() {
        super(Distance_f.class);
    }

    public NumberFunctional invoke(COORD_functional coord1, COORD_functional coord2) {
        this.coord1 = coord1;
        this.coord2 = coord2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.DISTANCE_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
