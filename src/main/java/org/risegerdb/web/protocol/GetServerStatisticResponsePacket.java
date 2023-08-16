package org.risegerdb.web.protocol;

public class GetServerStatisticResponsePacket extends BaseMsgPacket {

    private Long onlineUsers = 0L;

    private Long processingBytes = 0L;

    private Long InfoTransferSum = 0L;


    public Long getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(Long onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public Long getProcessingBytes() {
        return processingBytes;
    }

    public void setProcessingBytes(Long processingBytes) {
        this.processingBytes = processingBytes;
    }

    public Long getInfoTransferSum() {
        return InfoTransferSum;
    }

    public void setInfoTransferSum(Long infoTransferSum) {
        InfoTransferSum = infoTransferSum;
    }

    @Override
    public byte getCommand() {
        return Constant.GET_SERVER_STATISTIC_RESPONSE;
    }
}
