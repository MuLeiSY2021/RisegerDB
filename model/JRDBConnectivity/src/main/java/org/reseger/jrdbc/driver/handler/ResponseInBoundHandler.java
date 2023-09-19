package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.reseger.jrdbc.driver.connector.Connection;

import javax.xml.ws.Response;

public class ResponseInBoundHandler extends SimpleChannelInboundHandler<Response<?>> {
    private final Connection connection;

    public ResponseInBoundHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response<?> msg) throws Exception {
        connection.setResult(msg);
    }
}
