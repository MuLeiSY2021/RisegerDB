package org.riseger.protoctl.packet.request;

import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.packet.BasicPacket;
import org.riseger.protoctl.packet.PacketType;


public abstract class BasicRequest extends BasicPacket {

    public BasicRequest(PacketType packetType) {
        super(packetType);
    }

    public abstract Job warp();

}
