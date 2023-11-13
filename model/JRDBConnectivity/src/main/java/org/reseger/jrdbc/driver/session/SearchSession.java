package org.reseger.jrdbc.driver.session;

import lombok.Getter;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.compiler.command.USE;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.packet.response.SearchResponse;

@Getter
public class SearchSession extends Session<SearchResponse> {
    private USE sql;

    public SearchSession(Connection parent) {
        super(parent);
    }


    @Override
    public SearchResponse send() throws InterruptedException {
//        return super.send(new SearchRequest(sql));
        return null;
    }

    public USE use() {
        sql = new USE();
        return sql;
    }

    public ResultSet executeQuery(String userInput) {
        return null;
    }

    public int executeUpdate(String userInput) {
        return 0;
    }
}
