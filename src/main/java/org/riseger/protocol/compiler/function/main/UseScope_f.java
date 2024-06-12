package org.riseger.protocol.compiler.function.main;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.type.rectangleFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class UseScope_f extends Function_f implements mainFunctional {
    private rectangleFunctional rectangle;

    public UseScope_f() {
        super(UseScope_f.class);
    }

    public mainFunctional invoke(rectangleFunctional rectangle) {
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
