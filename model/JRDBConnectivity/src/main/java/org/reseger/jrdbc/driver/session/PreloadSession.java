package org.reseger.jrdbc.driver.session;

import org.reseger.jrdbc.driver.connector.Connector;
import org.riseger.protoctl.message.BasicMessage;
import org.riseger.protoctl.message.PreloadMessage;


public class PreloadSession implements Session {
    private transient final Connector parent;
    private String uri;

    public PreloadSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public BasicMessage send() throws InterruptedException {
        PreloadMessage message = new PreloadMessage();
        message.setUri(this.uri);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return parent.getResult();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
