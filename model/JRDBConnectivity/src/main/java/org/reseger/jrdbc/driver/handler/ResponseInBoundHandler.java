package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.packet.response.BasicResponse;

public class ResponseInBoundHandler extends SimpleChannelInboundHandler<BasicResponse> {
    private final Connection connection;

    public ResponseInBoundHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BasicResponse msg) {
        connection.setResult(msg);
    }
}
