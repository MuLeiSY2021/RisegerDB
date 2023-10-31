package org.riseger.protoctl.compiler.function.math;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.BOOL_functional;
import org.riseger.protoctl.compiler.function.type.NUMBER_functional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class SmallEqual_f extends Function_f implements BOOL_functional {
    private NUMBER_functional number1;

    private NUMBER_functional number2;

    public SmallEqual_f() {
        super(SmallEqual_f.class);
    }

    public BOOL_functional invoke(NUMBER_functional number1, NUMBER_functional number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.SMALL_EQUAL_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return true;
    }
}