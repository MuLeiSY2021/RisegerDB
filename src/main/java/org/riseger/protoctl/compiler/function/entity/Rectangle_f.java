package org.riseger.protoctl.compiler.function.entity;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.type.COORD_functional;
import org.riseger.protoctl.compiler.function.type.NUMBER_functional;
import org.riseger.protoctl.compiler.function.type.RECTANGLE_functional;
import org.riseger.protoctl.compiler.function.weight.ConstantWeight;

@Getter
public class Rectangle_f extends Function_f implements RECTANGLE_functional {

    private COORD_functional coord;

    private NUMBER_functional len;

    public Rectangle_f() {
        super(Rectangle_f.class);
    }

    public RECTANGLE_functional invoke(COORD_functional coord, NUMBER_functional len) {
        this.coord = coord;
        this.len = len;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.RECTANGLE_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
