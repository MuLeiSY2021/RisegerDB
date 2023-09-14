package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONBLE;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class DISTANCE extends FUNCTION implements NUMBER_FUNCTIONBLE {
    private COORD_FUNCTIONBLE coord1;

    private COORD_FUNCTIONBLE coord2;

    public DISTANCE() {
        super(DISTANCE.class);
    }

    public NUMBER_FUNCTIONBLE invoke(COORD_FUNCTIONBLE coord1, COORD_FUNCTIONBLE coord2) {
        //TODO:注意这里要标注为不同的函数定义数
        this.coord1 = coord1;
        this.coord2 = coord2;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) coord1);
        res.add((FUNCTION) coord2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.DISTANCE_WEIGHT;
    }
}
