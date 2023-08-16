package com.easyarch.communication.consolemanager;

import com.easyarch.protocol.GetGroupMembersRequestPacket;
import com.easyarch.protocol.GetServerStatisticResquestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class GetServerStatisticCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        GetServerStatisticResquestPacket getServerStatisticResquestPacket = new GetServerStatisticResquestPacket();

        channel.writeAndFlush(getServerStatisticResquestPacket);
    }
}
