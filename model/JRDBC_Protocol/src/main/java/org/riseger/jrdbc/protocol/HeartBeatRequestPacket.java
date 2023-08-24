package org.riseger.jrdbc.protocol;

public class HeartBeatRequestPacket extends BasePacket{
    public HeartBeatRequestPacket(byte type) {
        super(type);
    }

    @Override
    public byte getCommand() {
        return Constant.HEART_BEAT_REQUEST;
    }
}
