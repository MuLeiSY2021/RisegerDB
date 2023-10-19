package org.riseger.protoctl.search.function.key.graphic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Out_F extends Function_F implements BOOL_FUNCTIONAL {

    private RECTANGLE_FUNCTIONAL rect;

    public Out_F() {
        super(Out_F.class);
    }

    public BOOL_FUNCTIONAL invoke(RECTANGLE_FUNCTIONAL rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        List<Function_F> res = new LinkedList<>();
        res.add((Function_F) rect);
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
