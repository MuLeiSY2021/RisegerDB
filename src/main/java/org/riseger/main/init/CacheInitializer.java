package org.riseger.main.init;

import org.riseger.main.cache.entity.manager.CacheMaster;

public class CacheInitializer {

    public static void initialize() {
        CacheMaster.setINSTANCE(new CacheMaster());
    }
}
