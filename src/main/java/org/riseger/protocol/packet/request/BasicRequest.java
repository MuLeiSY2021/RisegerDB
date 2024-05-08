package org.riseger.protocol.packet.request;

import lombok.Getter;
import org.riseger.protocol.job.Job;
import org.riseger.protocol.packet.BasicPacket;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.packet.RequestType;


@Getter
public abstract class BasicRequest extends BasicPacket {

    private final RequestType requestType;

    public BasicRequest(PacketType packetType, RequestType requestType) {
        super(packetType);
        this.requestType = requestType;
    }

    public abstract Job warp();

}
