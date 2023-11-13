package org.reseger.jrdbc.driver.session;

import lombok.Getter;
import lombok.Setter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.packet.RequestType;
import org.riseger.protoctl.packet.request.BasicRequest;
import org.riseger.protoctl.packet.response.BasicResponse;

public abstract class Session<P extends BasicResponse> {
    protected transient final Connection parent;

    @Setter
    @Getter
    protected RequestType type;

    protected Session(Connection parent) {
        this.parent = parent;
    }

    abstract P send() throws InterruptedException;

    protected P send(BasicRequest request) throws InterruptedException {
        parent.getChannel().writeAndFlush(request);
        return (P) parent.getResult();
    }

    protected String getIpAddress() {
        return parent.getChannel().remoteAddress().toString();
    }
}
