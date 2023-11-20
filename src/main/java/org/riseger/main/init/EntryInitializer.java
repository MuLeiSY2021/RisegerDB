package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.riseger.main.entry.server.NettyServer;

public class EntryInitializer extends Initializer {
    private static final Logger LOG = Logger.getLogger(EntryInitializer.class);

    public EntryInitializer(String rootPath) {
        super(rootPath);
    }

    NettyServer server;

    public boolean init() {
        server = new NettyServer();
        Thread thread = new Thread(server);
        thread.setName("entry-initializer");
        thread.start();
        return true;
    }

    public void close() {
        server.close();
        LOG.info("Entry closed");
    }
}
