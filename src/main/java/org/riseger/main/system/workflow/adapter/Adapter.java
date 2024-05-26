package org.riseger.main.system.workflow.adapter;

import org.riseger.protocol.packet.request.BasicRequest;

public interface Adapter {
    void adapt(BasicRequest request);

}
