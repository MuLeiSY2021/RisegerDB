package org.reseger.jrdbc.driver.session;

import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.packet.request.BasicRequest;
import org.riseger.protoctl.packet.response.BasicResponse;

public abstract class Session<P extends BasicResponse<?>> {
    protected transient final Connection parent;

    protected Session(Connection parent) {
        this.parent = parent;
    }

    abstract P send() throws InterruptedException;

    protected P send(BasicRequest request) throws InterruptedException {
        parent.getChannel().writeAndFlush(request);
        return (P) parent.getResult();
    }


}
