package org.riseger.protoctl.search.function.key.graphic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class OUT extends FUNCTION implements BOOL_FUNCTIONAL {

    private RECTANGLE_FUNCTIONAL rect;

    public OUT() {
        super(OUT.class);
    }

    public BOOL_FUNCTIONAL invoke(RECTANGLE_FUNCTIONAL rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) rect);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.OUT_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
