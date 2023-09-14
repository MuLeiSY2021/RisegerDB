package org.riseger.protoctl.response;

import lombok.Getter;
import org.riseger.protoctl.message.BasicMessage;
import org.riseger.protoctl.message.MessageType;
import org.riseger.protoctl.request.Request;

@Getter
public class SearchResponse extends BasicMessage {

    private boolean success;

    private String message;

    public SearchResponse() {
        super(MessageType.PRELOAD_DB_RESPONSE);
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
