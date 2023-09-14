package org.riseger.protoctl.search.function.condition.math;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONBLE;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class BIG_EQUAL extends FUNCTION implements BOOL_FUNCTIONBLE {

    private NUMBER_FUNCTIONBLE number1;

    private NUMBER_FUNCTIONBLE number2;

    public BIG_EQUAL() {
        super(BIG_EQUAL.class);
    }

    public BOOL_FUNCTIONBLE invoke(NUMBER_FUNCTIONBLE number1, NUMBER_FUNCTIONBLE number2) {
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
        return ConstantWeight.BIG_EQUAL_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return true;
    }
}
