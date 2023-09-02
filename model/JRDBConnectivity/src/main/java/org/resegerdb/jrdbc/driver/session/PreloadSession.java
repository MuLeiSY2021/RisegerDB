package org.resegerdb.jrdbc.driver.session;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.resegerdb.jrdbc.driver.result.Result;
import org.riseger.protoctl.message.PreloadMessage;

import java.nio.charset.StandardCharsets;


public class PreloadSession implements Session {
    private String uri;

    private transient final Channel channel;

    private static final int SEND_MAX_LENGTH = 1500 - 10;

    private static final Gson gson = new Gson();


    public PreloadSession(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Result send() {
        PreloadMessage message = new PreloadMessage();
        message.setUri(uri);
        ByteBuf byteBuf = channel.alloc().buffer();
        byteBuf.writeBytes(gson.toJson(message).getBytes(StandardCharsets.UTF_8));
        channel.writeAndFlush(byteBuf);
        return null;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
