package org.riseger.jrdbc.protocol;

import io.netty.buffer.ByteBuf;

public interface Serializable {
    ByteBuf serialize();
}
