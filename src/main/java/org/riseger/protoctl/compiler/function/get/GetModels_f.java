package org.riseger.protoctl.compiler.function.get;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.mainFunctional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class GetModels_f extends Function_f implements mainFunctional {
    private String name;

    public GetModels_f() {
        super(GetModels_f.class);
    }

    public mainFunctional invoke(String name) {
        this.name = name;
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
