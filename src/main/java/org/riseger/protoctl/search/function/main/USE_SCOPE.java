package org.riseger.protoctl.search.function.main;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.MAIN_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class USE_SCOPE extends FUNCTION implements MAIN_FUNCTIONAL {
    private RECTANGLE_FUNCTIONAL rectangle;

    public USE_SCOPE() {
        super(USE_SCOPE.class);
    }

    public MAIN_FUNCTIONAL invoke(RECTANGLE_FUNCTIONAL rectangle) {
        this.rectangle = rectangle;
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
