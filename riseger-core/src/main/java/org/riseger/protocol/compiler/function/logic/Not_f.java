package org.riseger.protocol.compiler.function.logic;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.BOOL_functional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Not_f extends Function_f implements BOOL_functional {
    private BOOL_functional function;

    public Not_f() {
        super(Not_f.class);
    }

    public BOOL_functional invoke(BOOL_functional function) {
        this.function = function;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.NOT_WEIGHT(function.getWeight());
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
