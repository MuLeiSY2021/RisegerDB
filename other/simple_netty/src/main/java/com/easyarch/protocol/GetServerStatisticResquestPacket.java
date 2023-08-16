package com.easyarch.protocol;


public class GetServerStatisticResquestPacket extends BaseMsgPacket {

    @Override
    public byte getCommand() {
        return Constant.GET_SERVER_STATISTIC_REQUEST;
    }
}
