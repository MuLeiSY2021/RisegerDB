package org.riseger.protoctl.compiler.function.main;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.MAIN_functional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class PreWhere_f extends Function_f implements MAIN_functional {
    private String name;

    public PreWhere_f() {
        super(PreWhere_f.class);
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
