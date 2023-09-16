package org.riseger.protoctl.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.log4j.Logger;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.List;

public class ProtocolCodec extends ByteToMessageCodec<BasicPacket> {
    private static final Logger LOG = Logger.getLogger(ProtocolCodec.class);

    public ProtocolCodec() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BasicPacket msg, ByteBuf out) throws Exception {
        LOG.debug("Encoding message: " + msg.getType());
        out.writeByte(msg.getType().ordinal()); // MessageType
        byte[] bytes = JsonSerializer.serialize(msg);
        out.writeInt(bytes.length); // Data
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            PacketType packetType = PacketType.values()[in.readByte()];
            LOG.debug("Decoding message of type: " + packetType);

            byte[] data = new byte[in.readInt()];
            in.readBytes(data);
            BasicPacket bm = (BasicPacket) JsonSerializer.deserialize(data, packetType.getClazz());
            LOG.debug("Decoded message: " + bm);
            out.add(bm);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            throw e;
        }
    }
}
