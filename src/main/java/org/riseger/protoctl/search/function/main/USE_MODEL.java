package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.MAIN_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class USE_MODEL extends FUNCTION implements MAIN_FUNCTIONAL {
    private List<String> models;

    public USE_MODEL() {
        super(USE_MODEL.class);
    }

    public MAIN_FUNCTIONAL invoke(List<String> rectangle) {
        this.models = rectangle;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
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
