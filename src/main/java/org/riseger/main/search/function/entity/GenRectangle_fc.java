package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.search.function.type.Rectangle_fc;
import org.riseger.protoctl.search.function.FUNCTION;

import java.util.List;

public class GenRectangle_fc extends Rectangle_fc {
    public GenRectangle_fc(Class<? extends FUNCTION> parent) {
        super(parent);
    }

    @Override
    public List<MBRectangle_c> getResult(ElementManager elements) {
        return null;
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public boolean isResult(MBRectangle_c mb) {
        return false;
    }

    @Override
    public MBRectangle_c getResult() {
        return null;
    }
}
