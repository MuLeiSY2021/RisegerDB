package org.riseger.protoctl.message;

import lombok.Data;

@Data
public class ProtocolMessage {
    private MessageType messageType;

    private byte[] data;

    public ProtocolMessage(MessageType messageType, byte[] data) {
        this.messageType = messageType;
        this.data = data;
    }
}
