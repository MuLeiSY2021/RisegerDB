package org.risegerdb.web.websocket.entity;

import java.time.LocalDateTime;

public class ClientMessage {

    private String fromUserId;
    private String fromUserName;
    private String toUserId;
    private String toUserName;
    private String msg;
    private String currentDate;
    private int messageType;

    public ClientMessage(String fromUserId,
                         String fromUserName,
                         String toUserId,
                         String toUserName,
                         String msg,
                         int messageType) {
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.msg = msg;
        this.currentDate = LocalDateTime.now().toString();
        this.messageType = messageType;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
