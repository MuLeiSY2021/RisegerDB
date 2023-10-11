package org.riseger.protoctl.search.function.entity.field;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONBLE;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONBLE;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class RECT extends FUNCTION implements RECTANGLE_FUNCTIONBLE {

    private COORD_FUNCTIONBLE coord;

    private NUMBER_FUNCTIONBLE len;

    public RECT() {
        super(RECT.class);
    }

    public RECTANGLE_FUNCTIONBLE invoke(COORD_FUNCTIONBLE coord, NUMBER_FUNCTIONBLE len) {
        this.coord = coord;
        this.len = len;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) coord);
        res.add((FUNCTION) len);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.RECTANGLE_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
