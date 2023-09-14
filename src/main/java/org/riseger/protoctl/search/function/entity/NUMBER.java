package org.riseger.protoctl.search.function.entity;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.NUMBER_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.List;

@Getter
public class NUMBER extends FUNCTION implements NUMBER_FUNCTIONBLE {
    private Number number;

    public NUMBER() {
        super(NUMBER.class);
    }

    public NUMBER_FUNCTIONBLE invoke(Number number) {
        this.number = number;
        //TODO: 数字无法反序列化，需要自己写类型转换判定
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.NUMBER_WEIGHT;
    }
}
