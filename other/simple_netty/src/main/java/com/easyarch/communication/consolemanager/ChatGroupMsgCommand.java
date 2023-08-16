package com.easyarch.communication.consolemanager;

import com.easyarch.protocol.GroupMessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class ChatGroupMsgCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        GroupMessageRequestPacket groupMessageRequestPacket = new GroupMessageRequestPacket();

        System.out.println("请输入群组ID");

        String groupId = scanner.nextLine();
        String msg = scanner.nextLine();
        groupMessageRequestPacket.setGroupId(groupId);
        groupMessageRequestPacket.setMsg(msg);
        channel.writeAndFlush(groupMessageRequestPacket);
    }
}
