package org.riseger.protoctl.packet.request;

import lombok.Getter;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.packet.RequestType;


@Getter
public abstract class BasicRequest extends BasicPacket {

    private final RequestType requestType;

    public BasicRequest(PacketType packetType, RequestType requestType) {
        super(packetType);
        this.requestType = requestType;
    }

    public abstract Job warp();

}
