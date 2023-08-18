package org.resegerdb.jrdbc.driver.session;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import org.resegerdb.jrdbc.command.preload.builder.DatabaseBuilder;
import org.resegerdb.jrdbc.driver.result.Result;
import org.riseger.protoctl.request.PreloadRequest;
import org.riseger.protoctl.struct.model.Database;
import org.riseger.protoctl.utils.Utils;

import java.util.LinkedList;
import java.util.List;


public class PreloadSession implements Session {
    private final List<Database> databases = new LinkedList<Database>();

    private transient final Channel channel;

    private static final int SEND_MAX_LENGTH = 1500 - 10;

    public PreloadSession(Channel channel) {
        this.channel = channel;
    }

    public void addDatabase(Database database) {
        databases.add(database);
    }

    @Override
    public Result send() {

//        PreloadRequestPacket requestPacket = warpRequest();
//        ByteBuf byteBuf = channel.alloc().buffer();
//        byteBuf.writeBytes(requestPacket.serialize());
//        channel.writeAndFlush(byteBuf);
//        return new Result() {
//            @Override
//            public String getResult() {
//                return null;
//            }
//        };

        Utils.writeStringToFile(new Gson().toJson(new PreloadRequest(this.databases)), "src/main/resources/dataSource/0.json");
        return null;
    }

    public DatabaseBuilder buildDatabase() {
        return new DatabaseBuilder(this);
    }

}
