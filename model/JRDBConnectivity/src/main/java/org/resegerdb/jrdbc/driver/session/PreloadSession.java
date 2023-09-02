package org.resegerdb.jrdbc.driver.session;

import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.result.Result;
import org.riseger.protoctl.message.PreloadDatabaseRequest;


public class PreloadSession implements Session {
    private String uri;

    private transient final Connector parent;

    public PreloadSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public Result send() throws InterruptedException {
        PreloadDatabaseRequest message = new PreloadDatabaseRequest();
        message.setUri(this.uri);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return parent.getResult();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
