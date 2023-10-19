package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.MAIN_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class USE_MAP extends Function_F implements MAIN_FUNCTIONAL {
    private String name;

    public USE_MAP() {
        super(USE_MAP.class);
    }

    public MAIN_FUNCTIONAL invoke(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.MAIN_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
