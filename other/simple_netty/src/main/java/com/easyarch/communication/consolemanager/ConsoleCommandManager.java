package com.easyarch.communication.consolemanager;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleCommandManager implements ConsoleCommand {

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
//        consoleCommandMap.put("login", new LoginCommand());
        consoleCommandMap.put("sendToUser", new MessageCommand());
        consoleCommandMap.put("createGroup", new CreateChatGroupCommand());
        consoleCommandMap.put("joinGroup", new JoinChatGroupCommand());
        consoleCommandMap.put("sendGroupMsg", new ChatGroupMsgCommand());
        consoleCommandMap.put("quitGroup",new QuitChatGroupCommand());
        consoleCommandMap.put("getGroupMembers",new GetGroupMembersCommand());
        consoleCommandMap.put("getStatistic",new GetServerStatisticCommand());
    }


    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.println("请输入指令");
        String command = scanner.nextLine();
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.err.println("未知指令，请重新输入");
        }
    }
}
