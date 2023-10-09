package org.riseger.main.workflow.adapter;

import org.riseger.protoctl.packet.request.BasicRequest;

public interface Adapter {
    void adapt(BasicRequest request);

}
