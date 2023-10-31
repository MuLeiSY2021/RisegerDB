package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.MAIN_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class UseModel_f extends Function_f implements MAIN_functional {
    private List<String> models;

    public UseModel_f() {
        super(UseModel_f.class);
    }

    public MAIN_functional invoke(List<String> rectangle) {
        this.models = rectangle;
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
