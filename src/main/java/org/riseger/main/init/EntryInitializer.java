package org.riseger.main.init;

import org.riseger.main.entry.server.NettyServer;

public class EntryInitializer implements Initializer {
    public void init() {
        Thread thread = new Thread(new NettyServer());
        thread.setName("entry-initializer");
        thread.start();
    }
}
