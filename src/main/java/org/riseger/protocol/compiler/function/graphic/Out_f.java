package org.riseger.protocol.compiler.function.graphic;

import lombok.Getter;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.type.BOOL_functional;
import org.riseger.protocol.compiler.function.type.rectangleFunctional;
import org.riseger.protocol.compiler.function.weight.ConstantWeight;

@Getter
public class Out_f extends Function_f implements BOOL_functional, GraphicFunctional {

    private rectangleFunctional rect;

    public Out_f() {
        super(Out_f.class);
    }

    public BOOL_functional invoke(rectangleFunctional rect) {
        this.rect = rect;
        return this;
    }

    @Override
    public Integer getWeight() {
        return ConstantWeight.OUT_WEIGHT;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
