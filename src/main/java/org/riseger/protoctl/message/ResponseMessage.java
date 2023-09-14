package org.riseger.protoctl.message;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.entry.handler.TransponderHandler;

@Getter
@Setter
public abstract class ResponseMessage extends BasicMessage {
    private TransponderHandler<?, ?> transponder;

    public ResponseMessage(MessageType messageType) {
        super(messageType);
    }

    public void setTransponder(TransponderHandler<?, ?> transponder) {
        this.transponder = transponder;
    }
}
