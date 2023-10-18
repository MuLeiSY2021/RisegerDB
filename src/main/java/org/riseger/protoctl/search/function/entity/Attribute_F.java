package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class Attribute_F extends FUNCTION implements UNIVERSAL_FUNCTIONAL {
    private final UNIVERSAL_FUNCTIONAL attribute;

    public Attribute_F(UNIVERSAL_FUNCTIONAL attribute) {
        super(Attribute_F.class);
        this.attribute = attribute;
    }

    public static UNIVERSAL_FUNCTIONAL use(UNIVERSAL_FUNCTIONAL attribute) {
        return new Attribute_F(attribute);
//        String[] values = attribute.split("\\.");
//        this.model = values[0];
//        this.attribute = values[1];
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
