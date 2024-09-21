package org.riseger.protocol.compiler.function.preload;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Preload_f extends Function_f implements mainFunctional {

    public Preload_f() {
        super(Preload_f.class);
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
