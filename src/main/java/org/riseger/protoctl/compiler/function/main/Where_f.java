package org.riseger.protoctl.compiler.function.main;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction_f;
import org.riseger.protoctl.compiler.function.type.MAIN_functional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class Where_f extends Function_f implements MAIN_functional, ProcessorFunction_f {
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
