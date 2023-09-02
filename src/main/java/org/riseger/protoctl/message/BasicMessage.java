package org.riseger.protoctl.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BasicMessage implements Message{
    private final transient MessageType messageType;
    public BasicMessage(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}
