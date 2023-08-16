package org.risegerdb.web.protocol;

public class HeartBeatResponsePacket extends BaseMsgPacket {
    @Override
    public byte getCommand() {
        return Constant.HEART_BEAT_RESPONSE;
    }
}
