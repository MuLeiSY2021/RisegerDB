package com.easyarch.websocket;

import com.easyarch.entity.Session;
import com.easyarch.util.BuildWsMsg;
import com.easyarch.util.SessionUtil;
import com.easyarch.websocket.entity.ClientMessage;
import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WsChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Gson GSON = new Gson();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        ClientMessage clientMessage = GSON.fromJson(text, ClientMessage.class);
        if (clientMessage.getMessageType() == 3) {
            Channel toChannel = SessionUtil.getChannel(clientMessage.getToUserId());
            Session fromSession = SessionUtil.getSession(channelHandlerContext.channel());
            Session toSession = SessionUtil.getSession(toChannel);
            toChannel.writeAndFlush(BuildWsMsg.buildSingleMsg(fromSession.getUserId(), fromSession.getUserName(), toSession.getUserId(), toSession.getUserName(), clientMessage.getMsg(), 3));
            channelHandlerContext.channel().writeAndFlush(BuildWsMsg.buildSingleMsg(fromSession.getUserId(), fromSession.getUserName(), toSession.getUserId(), toSession.getUserName(), clientMessage.getMsg(), 0));
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            Session fromSession = SessionUtil.getSession(ctx.channel());
            ctx.channel().writeAndFlush(BuildWsMsg.buildSingleMsg(fromSession.getUserId(), fromSession.getUserName(), fromSession.getUserId(), fromSession.getUserName(), GSON.toJson(SessionUtil.getAllSession()), 1));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        SessionUtil.getSession()
    }
}
