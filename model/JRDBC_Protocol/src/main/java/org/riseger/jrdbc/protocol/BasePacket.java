package org.riseger.jrdbc.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufUtil;

public abstract class BasePacket {
    int magicnumber;

    byte type;

    public BasePacket(byte type) {
        this.type = type;
    }

    public ByteBuf serialize(ByteBuf innerBuf){
        magicnumber = innerBuf.hashCode();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer(5 + innerBuf.capacity());
        buffer.writeInt(this.magicnumber)
                .writeByte(this.type)
                .writeBytes(innerBuf);
        return buffer;
    }

    public abstract byte getCommand();
}
