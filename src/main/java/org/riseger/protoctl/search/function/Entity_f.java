package org.riseger.protoctl.search.function;

import org.riseger.protoctl.search.function.type.UNIVERSAL_functional;

public class Entity_f extends Function_f implements UNIVERSAL_functional {

    private final Object entity;

    public Entity_f(Object entity) {
        super(Entity_f.class);
        this.entity = entity;
    }

    public static UNIVERSAL_functional use(Object entity) {
        return new Entity_f(entity);
    }

    @Override
    public Integer getWeight() {
        return null;
    }

    @Override
    public boolean canSort() {
        return false;
    }
}
