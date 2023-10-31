package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

@Getter
public class And_f extends Function_f implements BOOL_functional {
    private BOOL_functional function1;

    private BOOL_functional function2;

    public And_f() {
        super(And_f.class);
    }

    public BOOL_functional invoke(BOOL_functional function1, BOOL_functional function2) {
        this.function1 = function1;
        this.function2 = function2;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.AND_WEIGHT(function1.getWeight(), function2.getWeight());
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
