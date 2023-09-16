package org.riseger.protoctl.packet;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.nio.charset.StandardCharsets;

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
        return new String(JsonSerializer.serialize(this), StandardCharsets.UTF_8);
    }
}
