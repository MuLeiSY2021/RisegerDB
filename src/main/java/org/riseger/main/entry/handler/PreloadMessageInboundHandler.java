package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;
import org.riseger.main.api.ApiHandlerManager;
import org.riseger.protoctl.packet.request.PreloadRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;

public class PreloadMessageInboundHandler extends SimpleChannelInboundHandler<PreloadRequest> {
    private final Logger LOG = Logger.getLogger(PreloadMessageInboundHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PreloadRequest msg) throws Exception {
        LOG.info("Received PreloadDatabaseRequest: " + msg);

        PreloadResponse response = new PreloadResponse();
        try {
            LOG.info("Processing PreloadDatabaseRequest...");
            ApiHandlerManager.INSTANCE.setPreloadRequest(msg);

            // Perform any necessary processing here...

            LOG.info("PreloadDatabaseRequest processed successfully.");
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            response.failed(e);
        }

        LOG.info("Sending PreloadDatabaseResponse: " + response);
        response.success("Successfully processed");
        ctx.channel().writeAndFlush(response);
    }
}
