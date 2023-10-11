package org.riseger.protoctl.packet.response;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.packet.PacketType;

@Setter
@Getter
public class TextSQLResponse extends BasicResponse {
    private String shellOutcome;

    public TextSQLResponse() {
        super(PacketType.TEXT_SQL_RESPONSE);
    }
}
