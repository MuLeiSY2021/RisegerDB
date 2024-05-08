package org.riseger.protocol.packet.response;

import lombok.Getter;
import org.riseger.protocol.packet.BasicPacket;
import org.riseger.protocol.packet.PacketType;

@Getter
public abstract class BasicResponse extends BasicPacket {
    private boolean success;

    private String exception;

    public BasicResponse(PacketType packetType) {
        super(packetType);
    }

    public void success() {
        this.success = true;
    }

    public void failed(Exception exception) {
        this.success = false;
        this.exception = exception.getMessage();
    }
}
