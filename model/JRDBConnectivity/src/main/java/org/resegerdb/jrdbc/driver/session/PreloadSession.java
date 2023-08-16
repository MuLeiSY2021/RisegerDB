package org.resegerdb.jrdbc.driver.session;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.resegerdb.jrdbc.driver.result.Result;
import org.resegerdb.jrdbc.struct.model.Database;
import org.resegerdb.jrdbc.command.preload.builder.DatabaseBuilder;
import org.resegerdb.jrdbc.protocol.PreloadRequestPacket;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;


public class PreloadSession implements Session {
    private final List<Database> databases = new LinkedList<Database>();

    private final Channel channel;

    private static final int SEND_MAX_LENGTH = 1500 - 10;

    public PreloadSession(Channel channel) {
        this.channel = channel;
    }

    public void addDatabase(Database database) {
        databases.add(database);
    }

    @Override
    public Result send() {
        PreloadRequestPacket requestPacket = warpRequest();
        ByteBuf byteBuf = channel.alloc().buffer();
        byteBuf.writeBytes(requestPacket.serialize());
        channel.writeAndFlush(byteBuf);

    }

    public DatabaseBuilder buildDatabase() {
        return new DatabaseBuilder(this);
    }

    private PreloadRequestPacket warpRequest() {
        PreloadRequestPacket tmp = new PreloadRequestPacket();
        byte[] content = new Gson().toJson(databases).getBytes(StandardCharsets.UTF_8);
        tmp.setContent(content);
        return tmp;
    }
}
