package org.riseger.protoctl.packet.response;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;

@Getter
@Setter
public abstract class BasicResponse<Msg> extends BasicPacket {
    private boolean success;

    private Msg message;

    private Exception exception;

    public BasicResponse(PacketType packetType) {
        super(packetType);
    }

    public void success(Msg message) {
        this.success = true;
        this.message = message;
    }

    public void failed(Exception exception) {
        this.success = false;
        this.exception = exception;
    }
}
