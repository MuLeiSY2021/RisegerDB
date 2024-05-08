package org.riseger.protocol.compiler.function.main;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.RECTANGLE_functional;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

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
