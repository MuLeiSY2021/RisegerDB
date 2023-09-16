package org.riseger.protoctl.packet;

import lombok.Getter;
import org.riseger.protoctl.packet.request.PreloadRequest;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;
import org.riseger.protoctl.packet.response.SearchResponse;

@Getter
public enum PacketType {
    PRELOAD_DB(PreloadRequest.class),
    PRELOAD_DB_RESPONSE(PreloadResponse.class),
    SEARCH(SearchRequest.class),
    SEARCH_RESPONSE(SearchResponse.class);


    private final Class<?> clazz;


    PacketType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
