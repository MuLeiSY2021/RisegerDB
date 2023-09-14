package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;
import org.riseger.main.api.ApiHandlerManager;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.response.SearchResponse;

public class SearchMessageInboundHandler extends SimpleChannelInboundHandler<SearchMessage> {
    private final Logger LOG = Logger.getLogger(SearchMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SearchMessage msg) throws Exception {
        LOG.info("Received SearchRequest: " + msg);

        SearchResponse response = new SearchResponse();
        try {
            LOG.info("Processing SearchRequest...");
            ApiHandlerManager.INSTANCE.setPreloadRequest(msg);

            // Perform any necessary processing here...

            LOG.info("SearchRequest processed successfully.");
        } catch (Exception e) {
            LOG.error("Error processing SearchRequest: " + e.getMessage() + e);
            response.failed(e);
        }

        LOG.info("Sending SearchRequest: " + response);
        response.success();
        ctx.channel().writeAndFlush(response);
    }
}
