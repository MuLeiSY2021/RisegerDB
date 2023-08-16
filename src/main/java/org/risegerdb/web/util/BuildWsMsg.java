package org.risegerdb.web.util;

import com.google.gson.Gson;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.risegerdb.web.websocket.entity.ClientMessage;

public class BuildWsMsg {
    private static final Gson GSON=new Gson();

    public static TextWebSocketFrame buildSingleMsg(String fromId,String fromName,String toId,String toName, String msgInfo,int type) {

        return new TextWebSocketFrame(GSON.toJson(new ClientMessage(fromId,fromName,toId,toName, msgInfo,type)));
    }

}
