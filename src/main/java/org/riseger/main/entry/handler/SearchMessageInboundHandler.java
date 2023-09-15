package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import org.riseger.main.api.ApiHandlerManager;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.response.SearchResponse;

import java.util.List;
import java.util.Map;

public class SearchMessageInboundHandler extends TransponderHandler<SearchMessage, Map<String,List<Element_c>>> {
    private final Logger LOG = Logger.getLogger(SearchMessageInboundHandler.class);

    private Map<String,List<Element_c>> result;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SearchMessage msg) throws Exception {
        LOG.info("Received SearchRequest: " + msg);

        SearchResponse response = new SearchResponse();
        try {
            LOG.info("Processing SearchRequest...");

            msg.setTransponder(this);
            ApiHandlerManager.INSTANCE.setSearchRequest(msg);

            LOG.info("SearchRequest processed successfully.");
        } catch (Exception e) {
            LOG.error("Error processing SearchRequest: " + e.getMessage() + e);
            response.failed(e);
        }

        LOG.info("Sending SearchRequest: " + response);
        response.success();
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public Map<String,List<Element_c>> getE() {
        try {
            super.sleep();
        } catch (InterruptedException e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setE(Map<String,List<Element_c>> result) {
        this.result = result;
        super.wake();
    }
}
