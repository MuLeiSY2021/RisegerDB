package com.easyarch.protocol;

public class JoinGroupRequestPacket extends BaseMsgPacket{

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Constant.JOIN_GROUP_REQUEST;
    }
}
