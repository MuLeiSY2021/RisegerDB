package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.MAIN_functional;
import org.riseger.protoctl.search.function.type.RECTANGLE_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class UseScope_f extends Function_f implements MAIN_functional {
    private RECTANGLE_functional rectangle;

    public UseScope_f() {
        super(UseScope_f.class);
    }

    public MAIN_functional invoke(RECTANGLE_functional rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.MAIN_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
