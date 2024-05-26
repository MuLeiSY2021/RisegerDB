package org.riseger.protocol.compiler.function.entity;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.UNIVERSAL_functional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Attribute_f extends Function_f implements UNIVERSAL_functional {

    public Attribute_f() {
        super(Attribute_f.class);
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.ATTRIBUTE_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
