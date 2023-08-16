package org.risegerdb.web.communication.consolemanager;

import io.netty.channel.Channel;
import org.risegerdb.web.protocol.LoginRequestPacket;
import org.risegerdb.web.util.LoginUtil;

import java.util.Scanner;

public class LoginCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        if (!LoginUtil.hasLogin(channel)) {
            System.out.println("请输入用户名：");
            String userName=scanner.nextLine();
            LoginRequestPacket loginRequestPacket=new LoginRequestPacket();
            loginRequestPacket.setUserName(userName);
            System.out.println("请输入密码：");
            String password=scanner.nextLine();
            loginRequestPacket.setPassword(password);
            channel.writeAndFlush(loginRequestPacket);
            waitLogin();
        }
    }

    private static void waitLogin(){
        //等待登录
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException ignore) {

        }
    }
}
