package org.riseger.protoctl.search.function.condition.math;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class SMALL_EQUAL extends FUNCTION implements BOOL_FUNCTIONAL {
    private NUMBER_FUNCTIONAL number1;

    private NUMBER_FUNCTIONAL number2;

    public SMALL_EQUAL() {
        super(SMALL_EQUAL.class);
    }

    public BOOL_FUNCTIONAL invoke(NUMBER_FUNCTIONAL number1, NUMBER_FUNCTIONAL number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) number1);
        res.add((FUNCTION) number2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.SMALL_EQUAL_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return true;
    }
}