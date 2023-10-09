package org.reseger.jrdbc.driver.session;

import lombok.Getter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.ResultSet;
import org.riseger.protoctl.search.command.USE;

@Getter
public class SearchSession extends Session<SearchResponse> {
    private USE sql;

    public SearchSession(Connection parent) {
        super(parent);
    }


    @Override
    public SearchResponse send() throws InterruptedException {
        return super.send(new SearchRequest(sql));
    }

    public USE use() {
        sql = new USE();
        return sql;
    }

    public ResultSet executeQuery(String userInput) throws SQLException {
        return null;
    }

    public int executeUpdate(String userInput) {
        return 0;
    }
}
