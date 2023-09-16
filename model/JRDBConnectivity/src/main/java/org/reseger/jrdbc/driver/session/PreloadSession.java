package org.reseger.jrdbc.driver.session;

import org.reseger.jrdbc.driver.connector.Connector;
import org.riseger.protoctl.packet.request.PreloadRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;


public class PreloadSession implements Session<PreloadResponse> {
    private transient final Connector parent;
    private String uri;

    public PreloadSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public PreloadResponse send() throws InterruptedException {
        PreloadRequest message = new PreloadRequest();
        message.setUri(this.uri);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return (PreloadResponse) parent.getResult();
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
