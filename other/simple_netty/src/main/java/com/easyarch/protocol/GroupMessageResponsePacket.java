package com.easyarch.protocol;

public class GroupMessageResponsePacket extends BaseMsgPacket {

    private String fromGroupId;
    private String msg;
    private String fromUser;

    public String getFromGroupId() {
        return fromGroupId;
    }

    public void setFromGroupId(String fromGroupId) {
        this.fromGroupId = fromGroupId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public byte getCommand() {
        return Constant.GROUP_MSG_RESPONSE;
    }
}
