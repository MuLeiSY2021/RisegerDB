package org.riseger.main.system.workflow.adapter;

import org.riseger.main.system.workflow.jobstack.CommonJobStack;
import org.riseger.protocol.packet.request.BasicRequest;

public class CommonAdapter implements Adapter {
    private final CommonJobStack stack;

    public CommonAdapter(CommonJobStack stack) {
        this.stack = stack;
    }

    @Override
    public void adapt(BasicRequest request) {
        stack.push(request.warp());
    }

}
