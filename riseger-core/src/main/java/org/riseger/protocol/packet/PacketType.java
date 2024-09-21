package org.riseger.protocol.packet;

import lombok.Getter;
import org.riseger.protocol.packet.request.CommandSQLRequest;
import org.riseger.protocol.packet.request.TextSQLRequest;
import org.riseger.protocol.packet.response.TextSQLResponse;

@Getter
public enum PacketType {
    TEXT_SQL(TextSQLRequest.class),
    TEXT_SQL_RESPONSE(TextSQLResponse.class),
    COMMAND_SQL(CommandSQLRequest.class);

    private final Class<?> clazz;


    PacketType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
