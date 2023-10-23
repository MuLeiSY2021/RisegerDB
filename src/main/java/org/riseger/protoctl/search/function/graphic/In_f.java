package org.riseger.protoctl.search.function.graphic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class In_f extends Function_f implements BOOL_FUNCTIONAL {

    private RECTANGLE_FUNCTIONAL rect;

    public In_f() {
        super(In_f.class);
    }

    public BOOL_FUNCTIONAL invoke(RECTANGLE_FUNCTIONAL rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public List<Function_f> getFunctions() {
        List<Function_f> res = new LinkedList<>();
        res.add((Function_f) rect);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.IN_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
