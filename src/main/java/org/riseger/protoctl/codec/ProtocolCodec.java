package org.riseger.protoctl.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.log4j.Logger;
import org.riseger.protoctl.message.BasicMessage;
import org.riseger.protoctl.message.MessageType;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.List;

public class ProtocolCodec extends ByteToMessageCodec<BasicMessage> {
    private static final Logger LOG = Logger.getLogger(ProtocolCodec.class);

    public ProtocolCodec() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, BasicMessage msg, ByteBuf out) throws Exception {
        LOG.debug("Encoding message: " + msg.getMessageType());
        out.writeByte(msg.getMessageType().ordinal()); // MessageType
        byte[] bytes = JsonSerializer.serialize(msg);
        out.writeInt(bytes.length); // Data
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            MessageType messageType = MessageType.values()[in.readByte()];
            LOG.debug("Decoding message of type: " + messageType);

            byte[] data = new byte[in.readInt()];
            in.readBytes(data);
            BasicMessage bm = (BasicMessage) JsonSerializer.deserialize(data, messageType.getClazz());
            LOG.debug("Decoded message: " + bm);
            out.add(bm);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }
    }
}
