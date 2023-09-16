package org.riseger.main.api.workflow.adapter;

import org.riseger.protoctl.packet.request.ResponseRequest;

public interface Adapter {
    void adapt(ResponseRequest request);

}
