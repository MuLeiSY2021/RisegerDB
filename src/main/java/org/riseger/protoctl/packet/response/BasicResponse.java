package org.riseger.protoctl.packet.response;

import lombok.Getter;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;

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
