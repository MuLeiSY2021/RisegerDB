package org.riseger.protoctl.packet.response;

import org.riseger.protoctl.packet.PacketType;

public class TextSQLResponse extends BasicResponse<String> {

    public TextSQLResponse() {
        super(PacketType.TEXT_SQL_RESPONSE);
    }
}
