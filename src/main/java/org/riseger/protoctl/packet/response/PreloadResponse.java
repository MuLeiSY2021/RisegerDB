package org.riseger.protoctl.packet.response;

import lombok.Getter;
import org.riseger.protoctl.packet.PacketType;

@Getter
public class PreloadResponse extends BasicResponse {

    public PreloadResponse() {
        super(PacketType.PRELOAD_DB_RESPONSE);
    }

    @Override
    public PacketType getType() {
        return super.getType();
    }

}
