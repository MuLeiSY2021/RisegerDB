package org.riseger.main.api.workflow.adapter;

import org.riseger.main.api.workflow.jobstack.CommonJobStack;
import org.riseger.protoctl.packet.request.ResponseRequest;

public class CommonAdapter implements Adapter {
    private final CommonJobStack stack;

    public CommonAdapter(CommonJobStack stack) {
        this.stack = stack;
    }

    @Override
    public void adapt(ResponseRequest request) {
        stack.push(request.warp());
    }

}
