package org.riseger.protoctl.packet;

import lombok.Getter;
import org.riseger.protoctl.packet.request.CommandSQLRequest;
import org.riseger.protoctl.packet.request.TextSQLRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.packet.response.TextSQLResponse;

@Getter
public enum PacketType {
    PRELOAD_DB_RESPONSE(PreloadResponse.class),
    SEARCH_RESPONSE(SearchResponse.class),
    TEXT_SQL(TextSQLRequest.class),
    TEXT_SQL_RESPONSE(TextSQLResponse.class), COMMAND_SQL(CommandSQLRequest.class);


    private final Class<?> clazz;


    PacketType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
