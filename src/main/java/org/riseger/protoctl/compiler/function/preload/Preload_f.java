package org.riseger.protoctl.compiler.function.preload;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.mainFunctional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

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
