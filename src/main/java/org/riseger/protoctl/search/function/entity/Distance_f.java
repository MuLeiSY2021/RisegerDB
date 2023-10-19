package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Distance_f extends Function_F implements NUMBER_FUNCTIONAL {
    private COORD_FUNCTIONAL coord1;

    private COORD_FUNCTIONAL coord2;

    public Distance_f() {
        super(Distance_f.class);
    }

    public NUMBER_FUNCTIONAL invoke(COORD_FUNCTIONAL coord1, COORD_FUNCTIONAL coord2) {
        this.coord1 = coord1;
        this.coord2 = coord2;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        List<Function_F> res = new LinkedList<>();
        res.add((Function_F) coord1);
        res.add((Function_F) coord2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.DISTANCE_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
