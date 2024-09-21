package org.riseger.protocol.compiler.function.get;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class GetMaps_f extends Function_f implements mainFunctional {
    private String name;

    public GetMaps_f() {
        super(GetMaps_f.class);
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
