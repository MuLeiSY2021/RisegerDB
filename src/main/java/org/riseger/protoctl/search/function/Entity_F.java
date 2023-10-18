package org.riseger.protoctl.search.function;

import org.riseger.protoctl.search.function.type.UNIVERSAL_FUNCTIONAL;

import java.util.List;

public class Entity_F extends FUNCTION implements UNIVERSAL_FUNCTIONAL {

    private Object[] entities;

    public Entity_F(Object... entities) {
        super(Entity_F.class);
        this.entities = entities;
    }

    public static UNIVERSAL_FUNCTIONAL use(Object... entities) {
        return new Entity_F(entities);
    }

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
