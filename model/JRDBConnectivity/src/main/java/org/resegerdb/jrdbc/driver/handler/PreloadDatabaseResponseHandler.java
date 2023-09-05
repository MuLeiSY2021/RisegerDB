package org.resegerdb.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.result.PreloadResult;
import org.riseger.protoctl.response.PreloadDatabaseResponse;

public class PreloadDatabaseResponseHandler extends SimpleChannelInboundHandler<PreloadDatabaseResponse> {
    private final Connector connector;

    public PreloadDatabaseResponseHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PreloadDatabaseResponse msg) throws Exception {
        connector.setResult(new PreloadResult(msg.isSuccess(), msg.getMessage()));
    }
}
