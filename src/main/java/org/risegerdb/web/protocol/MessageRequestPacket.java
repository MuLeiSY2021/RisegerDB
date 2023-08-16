package org.risegerdb.web.protocol;

public class MessageRequestPacket extends BaseMsgPacket {

    private String message;
    private String toUserId;


    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return Constant.MESSAGE_REQUEST;
    }
}
