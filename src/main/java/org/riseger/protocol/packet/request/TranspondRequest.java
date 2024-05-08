package org.riseger.protocol.packet.request;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.packet.RequestType;

@Getter
@Setter
public abstract class TranspondRequest extends BasicRequest {
    private TransponderHandler<? extends BasicRequest> transponder;

    public TranspondRequest(PacketType packetType, RequestType type) {
        super(packetType, type);
    }

    public void setTransponder(TransponderHandler<?> transponder) {
        this.transponder = transponder;
    }

}
