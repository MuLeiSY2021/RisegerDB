package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class AND extends FUNCTION implements BOOL_FUNCTIONBLE {
    private BOOL_FUNCTIONBLE function1;

    private BOOL_FUNCTIONBLE function2;

    public AND() {
        super(AND.class);
    }

    public BOOL_FUNCTIONBLE invoke(BOOL_FUNCTIONBLE function1, BOOL_FUNCTIONBLE function2) {
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
        return ConstantWeight.AND_WEIGHT(function1.getWeight(), function2.getWeight());
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
