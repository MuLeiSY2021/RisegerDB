package org.risegerdb.web.protocol;


public class QuitGroupRequestPacket extends BaseMsgPacket {

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public byte getCommand() {
        return Constant.QUIT_GROUP_REQUEST;
    }
}
