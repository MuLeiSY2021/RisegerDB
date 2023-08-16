package org.risegerdb.web.protocol;

import java.util.HashMap;
import java.util.Map;

public interface Constant {

    byte LOGIN_COMMAND = 1;
    byte LOGIN_RESPONSE_COMMAND = 2;
    byte MESSAGE_REQUEST = 3;
    byte MESSAGE_RESPONSE = 4;
    byte CREATE_GROUP_REQUEST = 5;
    byte CREATE_GROUP_RESPONSE = 6;
    byte JOIN_GROUP_REQUEST = 7;
    byte JOIN_GROUP_RESPONSE = 8;
    byte GROUP_MSG_REQUEST = 9;
    byte GROUP_MSG_RESPONSE = 10;
    byte HEART_BEAT_REQUEST = 11;
    byte HEART_BEAT_RESPONSE = 12;

    byte QUIT_GROUP_REQUEST = 13;

    byte QUIT_GROUP_RESPONSE = 14;

    byte GET_GROUP_MEMBERS_REQUEST = 15;

    byte GET_GROUP_MEMBERS_RESPONSE = 16;

    byte GET_SERVER_STATISTIC_REQUEST = 17;

    byte GET_SERVER_STATISTIC_RESPONSE = 18;

    byte JSON_SERIALIZER = 100;


    Map<Byte, Serializer> serializerMap = new HashMap<Byte, Serializer>() {
        {
            put(Constant.JSON_SERIALIZER, Serializer.DEFAULT);
        }
    };


    Map<Byte, Class<? extends BaseMsgPacket>> packetMap = new HashMap<Byte, Class<? extends BaseMsgPacket>>() {
        {
            put(Constant.LOGIN_COMMAND, LoginRequestPacket.class);
            put(Constant.LOGIN_RESPONSE_COMMAND, LoginResponsePacket.class);
            put(Constant.MESSAGE_REQUEST, MessageRequestPacket.class);
            put(Constant.MESSAGE_RESPONSE, MessageResponsePacket.class);
            put(Constant.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
            put(Constant.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
            put(Constant.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
            put(Constant.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
            put(Constant.GROUP_MSG_REQUEST, GroupMessageRequestPacket.class);
            put(Constant.GROUP_MSG_RESPONSE, GroupMessageResponsePacket.class);
            put(Constant.HEART_BEAT_RESPONSE, HeartBeatResponsePacket.class);
            put(Constant.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
            put(Constant.QUIT_GROUP_RESPONSE,QuitGroupResponsePacket.class);
            put(Constant.GET_GROUP_MEMBERS_REQUEST, GetGroupMembersRequestPacket.class);
            put(Constant.GET_GROUP_MEMBERS_RESPONSE,GetGroupMembersResponsePacket.class);
            put(Constant.GET_SERVER_STATISTIC_REQUEST, GetServerStatisticResquestPacket.class);
            put(Constant.GET_SERVER_STATISTIC_RESPONSE,GetServerStatisticResponsePacket.class);
        }
    };
}
