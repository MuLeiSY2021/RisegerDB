package org.riseger.protocol.compiler.function.main;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class WriteUpdateLog_f extends Function_f implements mainFunctional {
    private String name;

    public WriteUpdateLog_f() {
        super(WriteUpdateLog_f.class);
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
