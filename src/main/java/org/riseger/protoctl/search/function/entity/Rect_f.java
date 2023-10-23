package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Rect_f extends Function_f implements RECTANGLE_FUNCTIONAL {

    private COORD_FUNCTIONAL coord;

    private NUMBER_FUNCTIONAL len;

    public Rect_f() {
        super(Rect_f.class);
    }

    public RECTANGLE_FUNCTIONAL invoke(COORD_FUNCTIONAL coord, NUMBER_FUNCTIONAL len) {
        this.coord = coord;
        this.len = len;
        return this;
    }

    @Override
    public List<Function_f> getFunctions() {
        List<Function_f> res = new LinkedList<>();
        res.add((Function_f) coord);
        res.add((Function_f) len);
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
