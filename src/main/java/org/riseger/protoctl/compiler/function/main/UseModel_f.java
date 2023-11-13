package org.riseger.protoctl.compiler.function.main;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.mainFunctional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class UseModel_f extends Function_f implements mainFunctional {
    private List<String> models;

    public UseModel_f() {
        super(UseModel_f.class);
    }

    public mainFunctional invoke(List<String> rectangle) {
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
