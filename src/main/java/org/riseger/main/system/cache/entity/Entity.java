package org.riseger.main.system.cache.entity;

public class Entity implements Entity_f{
    private boolean isChanged = false;

    public void changeEntity() {
        this.isChanged = true;
    }

    public void resetChanged() {
        this.isChanged = false;
    }
}
