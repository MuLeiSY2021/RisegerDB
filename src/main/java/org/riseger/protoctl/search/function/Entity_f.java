package org.riseger.protoctl.search.function;

import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;

import java.util.List;

public class Entity_f extends Function_f implements UNIVERSAL_FUNCTIONAL {

    private Object[] entities;

    public Entity_f(Object... entities) {
        super(Entity_f.class);
        this.entities = entities;
    }

    public static UNIVERSAL_FUNCTIONAL use(Object... entities) {
        return new Entity_f(entities);
    }

    public List<Function_f> getFunctions() {
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
