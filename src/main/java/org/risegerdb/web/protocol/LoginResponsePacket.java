package org.risegerdb.web.protocol;

public class LoginResponsePacket extends BaseMsgPacket {

    private String userId;
    private String userName;
    //是否登录成功
    private boolean success;
    //失败原因
    private String info;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public byte getCommand() {
        return Constant.LOGIN_RESPONSE_COMMAND;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
