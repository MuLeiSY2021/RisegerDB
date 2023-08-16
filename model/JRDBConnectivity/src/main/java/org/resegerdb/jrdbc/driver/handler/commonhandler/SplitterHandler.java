package org.resegerdb.jrdbc.driver.handler.commonhandler;

import org.resegerdb.jrdbc.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class SplitterHandler extends LengthFieldBasedFrameDecoder {
    private static final int LENGTH_OFFSET = 7;
    private static final int LENGTH_FIELD = 4;


    public SplitterHandler() {
        super(Integer.MAX_VALUE, LENGTH_OFFSET, LENGTH_FIELD);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != PacketCodec.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
