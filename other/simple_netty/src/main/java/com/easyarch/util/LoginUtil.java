package com.easyarch.util;

import com.easyarch.protocol.Attributes;
import io.netty.channel.Channel;
import io.netty.util.Attribute;

public class LoginUtil {

    public static void markLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
        Statistic.addOnlineUser();
    }


    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> loginAttr = channel.attr(Attributes.LOGIN);
        return loginAttr.get() != null;
    }


    public static void Logout(Channel channel) {
        channel.attr(Attributes.LOGIN).set(null);
        Statistic.subOnlineUser();
    }
}
