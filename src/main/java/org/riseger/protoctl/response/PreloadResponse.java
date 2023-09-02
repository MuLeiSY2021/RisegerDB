package org.riseger.protoctl.response;

import org.riseger.protoctl.message.BasicMessage;
import org.riseger.protoctl.message.MessageType;
import org.riseger.protoctl.request.Request;

public class PreloadResponse extends BasicMessage {

    public boolean success;

    public String message;

    public PreloadResponse() {
        super(MessageType.TYPE_3_RESPONSE);
    }

    public void success() {
        this.success = true;
        this.message = "Successfully loaded";
    }

    public void failed(Exception e) {
        this.success = false;
        this.message = e.getMessage();
    }

    @Override
    public MessageType getType() {
        return super.getType();
    }

    @Override
    public Request warp() {
        return null;
    }
}
