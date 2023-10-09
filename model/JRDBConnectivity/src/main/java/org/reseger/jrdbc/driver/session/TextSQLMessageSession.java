package org.reseger.jrdbc.driver.session;

import lombok.Setter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.packet.request.TextSQLRequest;
import org.riseger.protoctl.packet.response.TextSQLResponse;

@Setter
public class TextSQLMessageSession extends Session<TextSQLResponse> {
    private String sqlText;

    public TextSQLMessageSession(Connection parent) {
        super(parent);
    }

    @Override
    public TextSQLResponse send() throws InterruptedException {
        return super.send(new TextSQLRequest(this.sqlText));
    }
}
