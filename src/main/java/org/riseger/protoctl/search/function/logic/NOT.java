package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class NOT extends FUNCTION implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function;

    public NOT() {
        super(NOT.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function) {
        this.function = function;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) function);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.NOT_WEIGHT(function.getWeight());
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
