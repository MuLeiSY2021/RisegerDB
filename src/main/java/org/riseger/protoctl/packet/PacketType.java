package org.riseger.protoctl.packet;

import lombok.Getter;
import org.riseger.protoctl.packet.request.PreloadRequest;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.packet.request.TextSQLRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.packet.response.TextSQLResponse;

@Getter
public enum PacketType {
    PRELOAD_DB(PreloadRequest.class),
    PRELOAD_DB_RESPONSE(PreloadResponse.class),
    SEARCH(SearchRequest.class),
    SEARCH_RESPONSE(SearchResponse.class),
    TEXT_SQL(TextSQLRequest.class),
    TEXT_SQL_RESPONSE(TextSQLResponse.class);


    private final Class<?> clazz;


    PacketType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
