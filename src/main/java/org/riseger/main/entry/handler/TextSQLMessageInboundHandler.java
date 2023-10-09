package org.riseger.main.entry.handler;

import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import org.riseger.main.entry.server.ApiHandlerManager;
import org.riseger.protoctl.packet.request.TextSQLRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;

public class TextSQLMessageInboundHandler extends TransponderHandler<TextSQLRequest, String> {

    private final Logger LOG = Logger.getLogger(TextSQLMessageInboundHandler.class);

    private String outboundMessage;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextSQLRequest msg) {
        LOG.info("Received TextSQLRequest: " + msg);

        TextSQLResponse response = new TextSQLResponse();
        try {
            LOG.info("Processing TextSQLRequest...");

            msg.setTransponder(this);

            ApiHandlerManager.INSTANCE.setMaintainRequest(msg);

            // Perform any necessary processing here...

            LOG.info("TextSQLRequest processed successfully.");
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            response.failed(e);
        }

        LOG.info("Sending TextSQLRequest: " + response);
        response.success(getOut());
        ctx.channel().writeAndFlush(response);
    }

    @Override
    public String getOut() {
        try {
            super.sleep();
        } catch (InterruptedException e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return outboundMessage;
    }

    @Override
    public void setOut(String outboundMessage) {
        this.outboundMessage = outboundMessage;
        super.wake();
    }
}
