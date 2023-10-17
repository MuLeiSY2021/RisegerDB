package org.riseger.protoctl.search.function.entity.field;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class RECT extends FUNCTION implements RECTANGLE_FUNCTIONAL {

    private COORD_FUNCTIONAL coord;

    private NUMBER_FUNCTIONAL len;

    public RECT() {
        super(RECT.class);
    }

    public RECTANGLE_FUNCTIONAL invoke(COORD_FUNCTIONAL coord, NUMBER_FUNCTIONAL len) {
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
