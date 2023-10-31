package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.MAIN_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class UseDatabase_f extends Function_f implements MAIN_functional {
    private String name;

    public UseDatabase_f() {
        super(UseDatabase_f.class);
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
