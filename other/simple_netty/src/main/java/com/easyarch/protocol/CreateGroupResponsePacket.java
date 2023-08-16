package com.easyarch.protocol;

import java.util.List;

public class CreateGroupResponsePacket extends BaseMsgPacket {

    private boolean success;
    private String groupId;
    private List<String> userNames;
    private String creator;


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

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

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    @Override
    public byte getCommand() {
        return Constant.CREATE_GROUP_RESPONSE;
    }
}
