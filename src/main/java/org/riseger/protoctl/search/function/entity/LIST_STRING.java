package org.riseger.protoctl.search.function.entity;

import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.STRINGS_FUNCTIONAL;

import java.util.List;

public class LIST_STRING extends FUNCTION implements STRINGS_FUNCTIONAL {


    @Override
    public List<FUNCTION> getFunctions() {
        return null;
    }

    @Override
    public Integer getWeight() {
        return null;
    }

    @Override
    public boolean canSkip() {
        return false;
    }
}
