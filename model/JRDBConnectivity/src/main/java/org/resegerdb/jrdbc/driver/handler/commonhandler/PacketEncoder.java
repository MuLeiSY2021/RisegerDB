package org.resegerdb.jrdbc.driver.handler.commonhandler;

import com.easyarch.protocol.BaseMsgPacket;
import com.easyarch.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Bas> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseMsgPacket packet, ByteBuf byteBuf) throws Exception {
        PacketCodec.INSTANCE.encode(byteBuf, packet);
    }
}
