package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.MAIN_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class Where_f extends Function_f implements MAIN_functional {
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
}
