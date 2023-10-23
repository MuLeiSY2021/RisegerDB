package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.Function_f;
import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class Attribute_f extends Function_f implements UNIVERSAL_FUNCTIONAL {
    private final UNIVERSAL_FUNCTIONAL attribute;

    public Attribute_f(UNIVERSAL_FUNCTIONAL attribute) {
        super(Attribute_f.class);
        this.attribute = attribute;
    }

    public static UNIVERSAL_FUNCTIONAL use(UNIVERSAL_FUNCTIONAL attribute) {
        return new Attribute_f(attribute);
//        String[] values = attribute.split("\\.");
//        this.model = values[0];
//        this.attribute = values[1];
    }

    @Override
    public List<Function_f> getFunctions() {
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
