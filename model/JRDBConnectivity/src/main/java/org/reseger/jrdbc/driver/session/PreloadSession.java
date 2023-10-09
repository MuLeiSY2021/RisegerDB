package org.reseger.jrdbc.driver.session;

import lombok.Getter;
import lombok.Setter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.packet.request.PreloadRequest;
import org.riseger.protoctl.packet.response.PreloadResponse;

@Getter
@Setter
public class PreloadSession extends Session<PreloadResponse> {
    private String uri;

    public PreloadSession(Connection parent) {
        super(parent);
    }

    @Override
    public PreloadResponse send() throws InterruptedException {
        return super.send(new PreloadRequest(this.uri));
    }
}
