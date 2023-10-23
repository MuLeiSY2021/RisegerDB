package org.riseger.protoctl.search.function;

import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;

import java.util.List;

public class Entity_f extends Function_f implements UNIVERSAL_FUNCTIONAL {

    private final Object entity;

    public Entity_f(Object entity) {
        super(Entity_f.class);
        this.entity = entity;
    }

    public static UNIVERSAL_FUNCTIONAL use(Object entity) {
        return new Entity_f(entity);
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
