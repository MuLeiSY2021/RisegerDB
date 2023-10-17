package org.riseger.protoctl.search.function.entity.basic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class ATTRIBUTE extends FUNCTION implements UNIVERSAL_FUNCTIONAL {
    private String model;

    private String attribute;

    public ATTRIBUTE() {
        super(ATTRIBUTE.class);
    }

    public UNIVERSAL_FUNCTIONAL invoke(String attribute) {
        String[] values = attribute.split("\\.");
        this.model = values[0];
        this.attribute = values[1];

        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.ATTRIBUTE_WEIGHT;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
