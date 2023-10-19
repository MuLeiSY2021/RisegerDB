package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_F;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Not_F extends Function_F implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function;

    public Not_F() {
        super(Not_F.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function) {
        this.function = function;
        return this;
    }

    @Override
    public List<Function_F> getFunctions() {
        List<Function_F> res = new LinkedList<>();
        res.add((Function_F) function);
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
