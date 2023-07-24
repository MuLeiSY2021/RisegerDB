package org.resegerdb.jrdbc.driver;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    public void connect(String host, String port) throws IOException {
        Socket socket = new Socket(host, Integer.parseInt(port));
    }

    public void close() {

    }

    void flush(Session session) {

    }

    public Session createSession() {
        return new Session();
    }
}
