package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.riseger.main.entry.server.ApiHandlerManager;
import org.riseger.protoctl.packet.request.SearchRequest;

public class SearchMessageInboundHandler extends TransponderHandler<SearchRequest> {
    @Override
    public void handle(ChannelHandlerContext ctx, SearchRequest msg) throws Exception {
        ApiHandlerManager.INSTANCE.setSearchRequest(msg);
    }
}
