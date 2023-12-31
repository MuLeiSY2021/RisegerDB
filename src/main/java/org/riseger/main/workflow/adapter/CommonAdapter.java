package org.riseger.main.workflow.adapter;

import org.riseger.main.workflow.jobstack.CommonJobStack;
import org.riseger.protoctl.packet.request.BasicRequest;

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
