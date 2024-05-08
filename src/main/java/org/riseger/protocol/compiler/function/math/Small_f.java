package org.riseger.protocol.compiler.function.math;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.BOOL_functional;
import org.riseger.protocol.compiler.function.type.NumberFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Small_f extends Function_f implements BOOL_functional {

    private NumberFunctional number1;

    private NumberFunctional number2;

    public Small_f() {
        super(Small_f.class);
    }

    public BOOL_functional invoke(NumberFunctional number1, NumberFunctional number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.SMALL_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return true;
    }
}