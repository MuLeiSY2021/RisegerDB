package org.riseger.protocol.compiler.function;

import lombok.Getter;
import org.riseger.protocol.compiler.function.type.UNIVERSAL_functional;

@Getter
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
