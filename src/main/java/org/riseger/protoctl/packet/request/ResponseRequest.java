package org.riseger.protoctl.packet.request;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;

@Getter
@Setter
public abstract class ResponseRequest extends BasicPacket {
    public ResponseRequest(PacketType packetType) {
        super(packetType);
    }

    private TransponderHandler<?, ?> transponder;

    public abstract Job warp();

    public void setTransponder(TransponderHandler<?, ?> transponder) {
        this.transponder = transponder;
    }
}
