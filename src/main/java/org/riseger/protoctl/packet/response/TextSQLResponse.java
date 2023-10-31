package org.riseger.protoctl.packet.response;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.compiler.result.ResultSet;

@Setter
@Getter
public class TextSQLResponse extends BasicResponse {
    private ResultSet shellOutcome;

    public TextSQLResponse() {
        super(PacketType.TEXT_SQL_RESPONSE);
    }
}
