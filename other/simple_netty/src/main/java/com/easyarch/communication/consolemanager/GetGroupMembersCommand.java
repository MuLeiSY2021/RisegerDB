package com.easyarch.communication.consolemanager;

import com.easyarch.protocol.GetGroupMembersRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class GetGroupMembersCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        GetGroupMembersRequestPacket getGroupMembersRequestPacket = new GetGroupMembersRequestPacket();

        System.out.println("请输入群组ID");

        String groupId = scanner.nextLine();
        getGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(getGroupMembersRequestPacket);
    }
}
