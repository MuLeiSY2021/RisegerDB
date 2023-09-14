package org.riseger.protoctl.search.function.key.graphic;

import lombok.Getter;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONBLE;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONBLE;
import org.riseger.protoctl.search.function.weight.ConstantWeight;

import java.util.LinkedList;
import java.util.List;

@Getter
public class IN extends FUNCTION implements BOOL_FUNCTIONBLE {

    private RECTANGLE_FUNCTIONBLE rect;

    public IN() {
        super(IN.class);
    }

    public BOOL_FUNCTIONBLE invoke(RECTANGLE_FUNCTIONBLE rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public List<FUNCTION> getFunctions() {
        List<FUNCTION> res = new LinkedList<>();
        res.add((FUNCTION) rect);
        return res;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.IN_WEIGHT;
    }


}
