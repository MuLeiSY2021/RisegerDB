package org.riseger.main.init;

import org.riseger.main.cache.manager.CacheMaster;

public class CacheInitializer implements Initializer{

    public void init() {
        CacheMaster.setINSTANCE(new CacheMaster());
    }
}
