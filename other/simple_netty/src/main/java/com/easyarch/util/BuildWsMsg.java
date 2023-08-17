package com.easyarch.util;

import com.easyarch.websocket.entity.ClientMessage;
import com.google.gson.Gson;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class BuildWsMsg {
    private static final Gson GSON = new Gson();

    public static TextWebSocketFrame buildSingleMsg(String fromId, String fromName, String toId, String toName, String msgInfo, int type) {

        return new TextWebSocketFrame(GSON.toJson(new ClientMessage(fromId, fromName, toId, toName, msgInfo, type)));
    }

}
