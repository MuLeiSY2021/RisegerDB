package org.riseger.protocol.compiler.function.entity;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.COORD_functional;
import org.riseger.protocol.compiler.function.type.NumberFunctional;
import org.riseger.protocol.compiler.function.type.rectangleFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Rectangle_f extends Function_f implements rectangleFunctional {

    private COORD_functional coord;

    private NumberFunctional len;

    public Rectangle_f() {
        super(Rectangle_f.class);
    }

    public rectangleFunctional invoke(COORD_functional coord, NumberFunctional len) {
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
