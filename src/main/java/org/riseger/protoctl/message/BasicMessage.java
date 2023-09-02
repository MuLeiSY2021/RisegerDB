package org.riseger.protoctl.message;

public abstract class BasicMessage implements Message{
    private final MessageType messageType;
    public BasicMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
