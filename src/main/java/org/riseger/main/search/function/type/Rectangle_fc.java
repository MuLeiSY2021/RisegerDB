package org.riseger.main.search.function.type;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.protoctl.search.function.FUNCTION;

public abstract class Rectangle_fc extends Function_c {

    public Rectangle_fc(Class<? extends FUNCTION> parent) {
        super(parent);
    }

    public abstract MBRectangle_c getResult();
}
