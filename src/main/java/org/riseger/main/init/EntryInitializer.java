package org.riseger.main.init;

import org.riseger.main.entry.server.NettyServer;
import org.riseger.main.entry.server.Server;

public class EntryInitializer implements Initializer{
    public void init() {
        Server entryServer = new NettyServer();
        entryServer.bootstrap();
    }
}
