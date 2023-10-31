package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.UNIVERSAL_functional;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

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
