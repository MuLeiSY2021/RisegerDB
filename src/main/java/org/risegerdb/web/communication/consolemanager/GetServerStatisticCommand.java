package org.risegerdb.web.communication.consolemanager;

import io.netty.channel.Channel;
import org.risegerdb.web.protocol.GetServerStatisticResquestPacket;

import java.util.Scanner;

public class GetServerStatisticCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        GetServerStatisticResquestPacket getServerStatisticResquestPacket = new GetServerStatisticResquestPacket();

        channel.writeAndFlush(getServerStatisticResquestPacket);
    }
}
