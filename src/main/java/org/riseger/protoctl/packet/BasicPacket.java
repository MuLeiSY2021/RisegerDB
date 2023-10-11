package org.riseger.protoctl.packet;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.serializer.JsonSerializer;

@Getter
@Setter
public class BasicPacket implements Packet {
    private final transient PacketType packetType;

    public BasicPacket(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    public PacketType getType() {
        return packetType;
    }

    @Override
    public String toString() {
        return JsonSerializer.serializeToString(this);
    }
}
