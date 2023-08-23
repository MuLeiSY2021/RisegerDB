package org.riseger.protoctl.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.log4j.Logger;
import org.riseger.protoctl.message.MessageType;
import org.riseger.protoctl.message.ProtocolMessage;

import java.util.Arrays;
import java.util.List;

public class ProtoctlCodec extends ByteToMessageCodec<ProtocolMessage> {
    private static final Logger log = Logger.getLogger(ProtoctlCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, ByteBuf out) throws Exception {
        out.writeInt(Arrays.hashCode(msg.getData()));
        out.writeByte(msg.getMessageType().ordinal()); // MessageType
        out.writeBytes(msg.getData()); // Data
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 5) {
            return; // Wait for more data
        }

        int header = in.readInt();
        MessageType messageType = MessageType.values()[in.readByte()];

        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        if(Arrays.hashCode(data) != header) {
            log.warn("Message Not correct");
            return;
        }

        ProtocolMessage protocolMessage = new ProtocolMessage(messageType,data);
        out.add(protocolMessage);
    }
}
