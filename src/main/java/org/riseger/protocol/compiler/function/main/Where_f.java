package org.riseger.protocol.compiler.function.main;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.ProcessorFunction_f;
import org.riseger.protocol.compiler.function.type.mainFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Where_f extends Function_f implements mainFunctional, ProcessorFunction_f {
    private String name;

    public Where_f() {
        super(Where_f.class);
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.MAIN_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }

    @Override
    public int[] getPostFunSize() {
        return null;
    }

    @Override
    public int getInsertFunSize() {
        return 1;
    }
}
