package org.riseger.protoctl.search.function.entity.field;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.COORD_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class COORD extends FUNCTION implements COORD_FUNCTIONAL {
    double number1;

    double number2;

    public COORD() {
        super(COORD.class);

    }

    public COORD_FUNCTIONAL invoke(double number1, double number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.COORD_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
