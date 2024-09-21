package org.riseger.protocol.packet.response;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.protocol.packet.PacketType;

@Setter
@Getter
public class TextSQLResponse extends BasicResponse {
    private ResultSet Result;

    public TextSQLResponse() {
        super(PacketType.TEXT_SQL_RESPONSE);
    }

}
