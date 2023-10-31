package org.riseger.protoctl.search.function.math;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_functional;
import org.riseger.protoctl.search.function.type.NUMBER_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class BigEqual_f extends Function_f implements BOOL_functional {

    private NUMBER_functional number1;

    private NUMBER_functional number2;

    public BigEqual_f() {
        super(BigEqual_f.class);
    }

    public BOOL_functional invoke(NUMBER_functional number1, NUMBER_functional number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.BIG_EQUAL_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return true;
    }
}
