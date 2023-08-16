package org.riseger.jrdbc.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.riseger.jrdbc.protocol.BasePacket;

import java.util.List;

@ChannelHandler.Sharable
public class BasePacketCodecHandler extends MessageToMessageCodec<ByteBuf, BasePacket> {

    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private BasePacketCodecHandler() {
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseMsgPacket packet, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.channel().alloc().ioBuffer();
        PacketCodec.INSTANCE.encode(byteBuf, packet);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        list.add(PacketCodec.INSTANCE.decode(byteBuf));
    }
}
