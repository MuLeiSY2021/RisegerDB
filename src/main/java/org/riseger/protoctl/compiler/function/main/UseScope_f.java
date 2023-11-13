package org.riseger.protoctl.compiler.function.main;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.RECTANGLE_functional;
import org.riseger.protoctl.compiler.function.type.mainFunctional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class UseScope_f extends Function_f implements mainFunctional {
    private RECTANGLE_functional rectangle;

    public UseScope_f() {
        super(UseScope_f.class);
    }

    public mainFunctional invoke(RECTANGLE_functional rectangle) {
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
