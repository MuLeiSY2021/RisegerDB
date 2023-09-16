package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;
import org.riseger.main.api.ApiHandlerManager;
import org.riseger.protoctl.message.PreloadMessage;
import org.riseger.protoctl.response.PreloadResponse;

public class PreloadMessageInboundHandler extends SimpleChannelInboundHandler<PreloadMessage> {
    private final Logger LOG = Logger.getLogger(PreloadMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PreloadMessage msg) throws Exception {
        LOG.info("Received PreloadDatabaseRequest: " + msg);

        PreloadResponse response = new PreloadResponse();
        try {
            LOG.info("Processing PreloadDatabaseRequest...");
            ApiHandlerManager.INSTANCE.setPreloadRequest(msg);

            // Perform any necessary processing here...

            LOG.info("PreloadDatabaseRequest processed successfully.");
        } catch (Exception e) {
            LOG.error("Error processing PreloadDatabaseRequest: " + e.getMessage() + e);
            response.failed(e);
        }

        LOG.info("Sending PreloadDatabaseResponse: " + response);
        response.success();
        ctx.channel().writeAndFlush(response);
    }
}
