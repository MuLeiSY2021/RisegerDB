package org.riseger.protoctl.message;

import org.riseger.protoctl.request.Request;

import java.io.IOException;

public interface Message {
    MessageType getType();

    Request warp() throws IOException;
}
