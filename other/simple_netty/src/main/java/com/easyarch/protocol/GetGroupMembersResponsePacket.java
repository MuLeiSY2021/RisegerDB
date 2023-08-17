package com.easyarch.protocol;

import java.util.List;

public class GetGroupMembersResponsePacket extends BaseMsgPacket {

    List<String> usersName;

    List<String> usersId;

    public List<String> getUsersName() {
        return usersName;
    }

    public void setUsersName(List<String> usersName) {
        this.usersName = usersName;
    }

    public List<String> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<String> usersId) {
        this.usersId = usersId;
    }

    @Override
    public byte getCommand() {
        return Constant.GET_GROUP_MEMBERS_RESPONSE;
    }

}
