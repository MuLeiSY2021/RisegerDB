package org.riseger.main.system.cache;

import lombok.Getter;

@Getter
public class HolisticStorageEntity implements HolisticStorageEntity_i {

    private boolean isChanged;

    public HolisticStorageEntity() {
        this.isChanged = false;
    }

    public void changeEntity() {
        this.isChanged = true;
    }

    public void resetChanged() {
        this.isChanged = false;
    }
}













