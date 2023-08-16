package com.easyarch.util;

import com.easyarch.entity.Session;
import com.easyarch.protocol.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {

    private static final Map<String, Channel> userMap = new ConcurrentHashMap<>();

    private static final Map<String, ChannelGroup> groupMap = new ConcurrentHashMap<>();

    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userMap.put(session.getUserId(), channel);
        sessionMap.put(session.getUserId(), session);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (LoginUtil.hasLogin(channel)) {
            Attribute<Session> sessionAttribute = channel.attr(Attributes.SESSION);
            System.out.println(sessionAttribute.get().getUserId() + "下线");
            userMap.remove(sessionAttribute.get().getUserId());
            sessionMap.remove(sessionAttribute.get().getUserId());
            sessionAttribute.set(null);
            LoginUtil.Logout(channel);
        }
    }


    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userMap.get(userId);
    }


    public static void createGroup(String groupId, ChannelGroup channelGroup) {
        groupMap.put(groupId, channelGroup);
    }

    public static ChannelGroup joinGroup(String groupId, Channel channel) {
        ChannelGroup channelGroup = groupMap.get(groupId);
        channelGroup.add(channel);
        return channelGroup;
    }

    public static ChannelGroup quitGroup(String groupId, Channel channel) {
        ChannelGroup channelGroup = groupMap.get(groupId);
        channelGroup.remove(channel);
        return channelGroup;
    }

    public static ChannelGroup getGroup(String groupId) {
        return groupMap.get(groupId);
    }


    public static List<Session> getAllSession() {
        return new ArrayList<>(sessionMap.values());
    }
}
