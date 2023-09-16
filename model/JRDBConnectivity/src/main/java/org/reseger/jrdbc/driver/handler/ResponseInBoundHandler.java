package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.reseger.jrdbc.driver.connector.Connector;
import org.riseger.protoctl.message.BasicMessage;

public class ResponseInBoundHandler extends SimpleChannelInboundHandler<BasicMessage> {
    private final Connector connector;

    public ResponseInBoundHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BasicMessage msg) throws Exception {
        connector.setResult(msg);
    }
}
