package com.easyarch.protocol;

public class GroupMessageRequestPacket extends BaseMsgPacket {

    private String groupId;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Constant.GROUP_MSG_REQUEST;
    }
}
