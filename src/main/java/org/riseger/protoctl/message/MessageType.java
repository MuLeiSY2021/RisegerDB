package org.riseger.protoctl.message;

import lombok.Getter;
import org.riseger.protoctl.response.PreloadDatabaseResponse;

@Getter
public enum MessageType {
    PRELOAD_DB(PreloadDatabaseMessage.class),
    PRELOAD_DB_RESPONSE(PreloadDatabaseResponse.class),
    SEARCH(SearchMessage.class);

    private final Class<?> clazz;


    MessageType(Class<?> clazz) {
        this.clazz = clazz;
    }
}
