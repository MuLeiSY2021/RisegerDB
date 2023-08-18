package org.riseger.protoctl.message;

public class PreloadMessage implements Message{
    public static final MessageType TYPE = MessageType.TYPE_0_PRELOAD;

    private String uri;

    @Override
    public MessageType getType() {
        return TYPE;
    }
}
