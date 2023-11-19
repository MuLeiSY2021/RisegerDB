package org.riseger.protoctl.packet;

import lombok.Getter;
import org.riseger.protoctl.packet.request.CommandSQLRequest;
import org.riseger.protoctl.packet.request.TextSQLRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;

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
