package org.reseger.jrdbc.driver.session;

import org.reseger.jrdbc.driver.connector.Connector;
import org.reseger.jrdbc.driver.result.Result;
import org.riseger.protoctl.message.PreloadDatabaseMessage;


public class PreloadSession implements Session {
    private transient final Connector parent;
    private String uri;

    public PreloadSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public Result send() throws InterruptedException {
        PreloadDatabaseMessage message = new PreloadDatabaseMessage();
        message.setUri(this.uri);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return parent.getResult();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
