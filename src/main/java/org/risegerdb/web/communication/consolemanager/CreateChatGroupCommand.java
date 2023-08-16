package org.risegerdb.web.communication.consolemanager;

import io.netty.channel.Channel;
import org.risegerdb.web.protocol.CreateGroupRequestPacket;

import java.util.Arrays;
import java.util.Scanner;

public class CreateChatGroupCommand implements ConsoleCommand {

    private static final String SPLITTER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();
        System.out.println("建立群组，请输入用户ID，多个ID以英文逗号分割");
        String userIds = scanner.nextLine();
        packet.setUserIds(Arrays.asList(userIds.split(SPLITTER)));
        channel.writeAndFlush(packet);
    }
}
