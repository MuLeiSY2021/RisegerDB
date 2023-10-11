package org.riseger.protoctl.packet.request;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.packet.PacketType;

@Getter
@Setter
public abstract class TranspondRequest extends BasicRequest {
    private TransponderHandler<? extends BasicRequest> transponder;

    public TranspondRequest(PacketType packetType) {
        super(packetType);
    }

    public void setTransponder(TransponderHandler<?> transponder) {
        this.transponder = transponder;
    }

}
