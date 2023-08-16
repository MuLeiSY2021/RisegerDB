package org.risegerdb.web.websocket;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.risegerdb.web.entity.Session;
import org.risegerdb.web.util.BuildWsMsg;
import org.risegerdb.web.util.SessionUtil;
import org.risegerdb.web.websocket.entity.ClientMessage;

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
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            Session fromSession = SessionUtil.getSession(ctx.channel());
            ctx.channel().writeAndFlush(BuildWsMsg.buildSingleMsg(fromSession.getUserId(), fromSession.getUserName(), fromSession.getUserId(), fromSession.getUserName(), GSON.toJson(SessionUtil.getAllSession()), 1));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        SessionUtil.getSession()
    }
}
