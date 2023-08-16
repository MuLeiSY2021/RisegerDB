package org.riseger.jrdbc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;


public class PreloadRequestPacket extends BasePacket implements Serializable {
    byte status;

    int length;

    byte[] content;

    private static final byte PRELOAD = 0;

    public PreloadRequestPacket() {
        super(PRELOAD);
    }

    public PreloadRequestPacket setContent(byte[] content){
        this.content = content;
        this.length = content.length;
        return this;
    }

    @Override
    public ByteBuf serialize() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(1 +  4 + length);
        byteBuf.writeByte(status)
                .writeInt(length)
                .writeBytes(content);

        return super.serialize(byteBuf);
    }

    public PreloadRequestPacket deserialize(ChannelHandlerContext ctx) {
        return null;
    }
}
