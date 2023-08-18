package org.riseger.protoctl.message;

public class ResponseMessage implements Message {
    private static final MessageType type = MessageType.TYPE_3_RESPONSE;
    @Override
    public MessageType getType() {
        return type;
    }
}
