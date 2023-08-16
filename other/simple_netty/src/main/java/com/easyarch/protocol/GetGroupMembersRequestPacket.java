package com.easyarch.protocol;

public class GetGroupMembersRequestPacket extends BaseMsgPacket{

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Constant.GET_GROUP_MEMBERS_REQUEST;
    }
}
