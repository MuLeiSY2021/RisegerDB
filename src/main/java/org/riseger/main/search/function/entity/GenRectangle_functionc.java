package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.search.function.type.RectangleFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class GenRectangle_functionc extends RectangleFunction_c {
    public GenRectangle_functionc(int indexStart) {
        super(indexStart);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public MBRectangle_c getResult(Element_c element) {
        return null;
    }
}
