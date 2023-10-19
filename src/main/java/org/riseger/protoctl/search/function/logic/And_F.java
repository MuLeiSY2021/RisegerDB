package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class And_F extends Function_F implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function1;

    private BOOL_FUNCTIONAL function2;

    public And_F() {
        super(And_F.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function1, BOOL_FUNCTIONAL function2) {
        this.function1 = function1;
        this.function2 = function2;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        List<Function_F> res = new LinkedList<>();
        res.add((Function_F) function1);
        res.add((Function_F) function2);
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
