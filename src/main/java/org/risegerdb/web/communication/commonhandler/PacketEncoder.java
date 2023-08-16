package org.risegerdb.web.communication.commonhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.risegerdb.web.protocol.BaseMsgPacket;
import org.risegerdb.web.protocol.PacketCodec;

public class PacketEncoder extends MessageToByteEncoder<BaseMsgPacket> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseMsgPacket packet, ByteBuf byteBuf) throws Exception {
        PacketCodec.INSTANCE.encode(byteBuf, packet);
    }
}
