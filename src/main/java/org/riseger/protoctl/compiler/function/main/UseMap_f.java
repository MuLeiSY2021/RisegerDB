package org.riseger.protoctl.compiler.function.main;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.MAIN_functional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class UseMap_f extends Function_f implements MAIN_functional {
    private String name;

    public UseMap_f() {
        super(UseMap_f.class);
    }

    public MAIN_functional invoke(String name) {
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
