package com.easyarch.protocol;

import java.util.List;

public class CreateGroupRequestPacket extends BaseMsgPacket {

    private String creatorId;
    private List<String> userIds;


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }


    @Override
    public byte getCommand() {
        return Constant.CREATE_GROUP_REQUEST;
    }
}
