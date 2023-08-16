package org.risegerdb.web.communication.consolemanager;

import io.netty.channel.Channel;
import org.risegerdb.web.protocol.QuitGroupRequestPacket;

import java.util.Scanner;

public class QuitChatGroupCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();

        System.out.println("请输入要退出的群组ID");

        String groupId = scanner.nextLine();
        quitGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(quitGroupRequestPacket);
    }
}
