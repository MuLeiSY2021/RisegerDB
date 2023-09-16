package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.reseger.jrdbc.driver.connector.Connector;

import javax.xml.ws.Response;

public class ResponseInBoundHandler extends SimpleChannelInboundHandler<Response<?>> {
    private final Connector connector;

    public ResponseInBoundHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response<?> msg) throws Exception {
        connector.setResult(msg);
    }
}
