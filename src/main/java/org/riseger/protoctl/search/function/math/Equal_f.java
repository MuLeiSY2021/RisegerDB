package org.riseger.protoctl.search.function.math;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Equal_f extends Function_f implements BOOL_FUNCTIONAL {

    private NUMBER_FUNCTIONAL number1;

    private NUMBER_FUNCTIONAL number2;

    public Equal_f() {
        super(Equal_f.class);
    }

    public BOOL_FUNCTIONAL invoke(NUMBER_FUNCTIONAL number1, NUMBER_FUNCTIONAL number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }

    @Override
    public List<Function_f> getFunctions() {
        List<Function_f> res = new LinkedList<>();
        res.add((Function_f) number1);
        res.add((Function_f) number2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.EQUAL_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return true;
    }
}