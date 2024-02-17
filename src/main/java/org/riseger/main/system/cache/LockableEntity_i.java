package org.riseger.main.system.cache;

public interface LockableEntity_i {

    void read();

    void unread();

    void write();

    void unwrite();
}
