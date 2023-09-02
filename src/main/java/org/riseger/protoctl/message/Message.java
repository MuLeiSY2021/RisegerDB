package org.riseger.protoctl.message;

import org.riseger.protoctl.request.Request;

public interface Message {
    MessageType getType();

    Request warp();
}
