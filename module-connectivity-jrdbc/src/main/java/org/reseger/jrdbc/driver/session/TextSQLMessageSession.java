package org.reseger.jrdbc.driver.session;

import lombok.Setter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protocol.packet.request.TextSQLRequest;
import org.riseger.protocol.packet.response.TextSQLResponse;

@Setter
public class TextSQLMessageSession extends Session<TextSQLResponse> {
    private String sqlText;

    public TextSQLMessageSession(Connection parent) {
        super(parent);
    }

    @Override
    public TextSQLResponse send() throws InterruptedException {
        return super.send(new TextSQLRequest(this.sqlText, super.getType(), super.getIpAddress()));
    }

    public TextSQLResponse send(long nanosTimeout) throws InterruptedException {
        return super.send(new TextSQLRequest(this.sqlText, super.getType(), super.getIpAddress()), nanosTimeout);
    }
}
