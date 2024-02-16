package org.riseger.main.system.cache;

public interface CacheEntity_i {

    void changeEntity();

    void resetChanged();

    void read();

    void unread();

    void write();

    void unwrite();
}
