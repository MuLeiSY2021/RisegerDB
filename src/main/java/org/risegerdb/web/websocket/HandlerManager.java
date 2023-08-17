package org.risegerdb.web.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HandlerManager extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//http 协议编解码器
        //鉴权处理器
        ch.pipeline().addLast("auth", new WebSocketAuthHandler());
        //大数据的分区传输
        //websocket协议处理器
        ch.pipeline().addLast("websocket", new WebSocketServerProtocolHandler("/imServer"));
        //自定义消息处理器
        ch.pipeline().addLast("my-handler", new WsChannelHandler());
    }
}
