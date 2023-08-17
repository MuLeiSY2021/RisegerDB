package com.easyarch.communication.consolemanager;

import com.easyarch.protocol.MessageRequestPacket;
import io.netty.channel.Channel;

import java.util.Scanner;

public class MessageCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        String toUserId = scanner.nextLine();
        String message = scanner.nextLine();
        MessageRequestPacket requestPacket = new MessageRequestPacket();
        requestPacket.setMessage(message);
        requestPacket.setToUserId(toUserId);
        channel.writeAndFlush(requestPacket);
    }
}
