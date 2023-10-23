package org.riseger.protoctl.search.function.logic;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Not_f extends Function_f implements BOOL_FUNCTIONAL {
    private BOOL_FUNCTIONAL function;

    public Not_f() {
        super(Not_f.class);
    }

    public BOOL_FUNCTIONAL invoke(BOOL_FUNCTIONAL function) {
        this.function = function;
        return this;
    }

    @Override
    public List<Function_f> getFunctions() {
        List<Function_f> res = new LinkedList<>();
        res.add((Function_f) function);
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
