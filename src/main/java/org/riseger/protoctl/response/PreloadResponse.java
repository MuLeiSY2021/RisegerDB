package org.riseger.protoctl.response;

import lombok.Getter;
import org.riseger.protoctl.message.BasicMessage;

@Getter
public class PreloadResponse extends BasicMessage {

    private boolean success;

    private String message;

    public PreloadResponse() {
        super(PreloadResponse.class);
    }

    public void success() {
        this.success = true;
        this.message = "Successfully loaded";
    }

    public void failed(Exception e) {
        this.success = false;
        this.message = e.getMessage();
    }

}
