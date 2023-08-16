package org.risegerdb.web.protocol;

public class HeartBeatRequestPacket extends BaseMsgPacket {
    @Override
    public byte getCommand() {
        return Constant.HEART_BEAT_REQUEST;
    }
}
