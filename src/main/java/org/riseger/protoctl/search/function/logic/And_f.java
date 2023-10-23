package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class And_f extends Function_f implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function1;

    private BOOL_FUNCTIONAL function2;

    public And_f() {
        super(And_f.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function1, BOOL_FUNCTIONAL function2) {
        this.function1 = function1;
        this.function2 = function2;
        return this;
    }

    @Override
    public List<Function_f> getFunctions() {
        List<Function_f> res = new LinkedList<>();
        res.add((Function_f) function1);
        res.add((Function_f) function2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.AND_WEIGHT(function1.getWeight(), function2.getWeight());
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
