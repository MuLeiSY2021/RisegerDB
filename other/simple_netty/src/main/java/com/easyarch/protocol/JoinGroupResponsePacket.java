package com.easyarch.protocol;

public class JoinGroupResponsePacket extends BaseMsgPacket {

    private boolean success;
    private String groupId;
    private String userName;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public byte getCommand() {
        return Constant.JOIN_GROUP_RESPONSE;
    }
}
