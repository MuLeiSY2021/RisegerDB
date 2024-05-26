package org.riseger.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.log4j.Logger;
import org.riseger.protocol.packet.BasicPacket;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.serializer.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProtocolCodec extends ByteToMessageCodec<BasicPacket> {
    private static final Logger LOG = Logger.getLogger(ProtocolCodec.class);

    public ProtocolCodec() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BasicPacket msg, ByteBuf out) {
        LOG.info("Accept message from" + ctx.channel().remoteAddress() + " Message Type:" + msg.getType());
        LOG.debug("Encoding message: " + msg.getType());
        out.writeByte(msg.getType().ordinal()); // MessageType
        byte[] bytes = JsonSerializer.serialize(msg);
        out.writeInt(bytes.length); // Data
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        PacketType packetType = PacketType.values()[in.readByte()];
        LOG.info("Decoding message of type: " + packetType);
        int len = in.readInt();
        byte[] data = new byte[len];
        int i = 0;
        while (i != len) {
            if (in.isReadable()) {
                data[i++] = in.readByte();
            } else {
                in.resetReaderIndex();
                return;
            }
        }

        String message = new String(data, StandardCharsets.UTF_8);
        try {
            BasicPacket bm = (BasicPacket) JsonSerializer.deserialize(data, packetType.getClazz());
            LOG.debug("Decoded message: " + bm);
            out.add(bm);
        } catch (Exception e) {
            LOG.error("ErrorJson:" + message, e);
            throw e;
        }
    }
}
