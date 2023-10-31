package org.riseger.protoctl.search.function.graphic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_functional;
import org.riseger.protoctl.search.function.type.RECTANGLE_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class Out_f extends Function_f implements BOOL_functional {

    private RECTANGLE_functional rect;

    public Out_f() {
        super(Out_f.class);
    }

    public BOOL_functional invoke(RECTANGLE_functional rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.OUT_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
