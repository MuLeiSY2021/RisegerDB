package org.reseger.jrdbc.driver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.reseger.jrdbc.driver.connector.Connector;
import org.reseger.jrdbc.driver.result.PreloadResult;
import org.riseger.protoctl.response.PreloadDatabaseResponse;

public class SearchResponseHandler extends SimpleChannelInboundHandler<PreloadDatabaseResponse> {
    private final Connector connector;

    public SearchResponseHandler(Connector connector) {
        this.connector = connector;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PreloadDatabaseResponse msg) throws Exception {
        connector.setResult(new PreloadResult(msg.isSuccess(), msg.getMessage()));
    }
}
