package org.risegerdb.web.protocol;

public abstract class BaseMsgPacket {

    private byte version = 1;

    public abstract byte getCommand();

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}
