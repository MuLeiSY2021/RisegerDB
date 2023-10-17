package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class OR extends FUNCTION implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function1;

    private BOOL_FUNCTIONAL function2;

    public OR() {
        super(OR.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function1, BOOL_FUNCTIONAL function2) {
        this.function1 = function1;
        this.function2 = function2;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) function1);
        res.add((FUNCTION) function2);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.OR_WEIGHT(function1.getWeight(), function2.getWeight());
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
