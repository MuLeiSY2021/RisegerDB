package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.COORD_functional;
import org.riseger.protoctl.search.function.type.NUMBER_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class Distance_f extends Function_f implements NUMBER_functional {
    private COORD_functional coord1;

    private COORD_functional coord2;

    public Distance_f() {
        super(Distance_f.class);
    }

    public NUMBER_functional invoke(COORD_functional coord1, COORD_functional coord2) {
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
