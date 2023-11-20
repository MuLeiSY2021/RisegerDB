package org.riseger.main.init;

import org.riseger.main.entry.server.NettyServer;

public class EntryInitializer extends Initializer {

    public EntryInitializer(String rootPath) {
        super(rootPath);
    }

    public boolean init() {
        Thread thread = new Thread(new NettyServer());
        thread.setName("entry-initializer");
        thread.start();
        return true;
    }
}
