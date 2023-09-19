package org.reseger.jrdbc.driver.session;

import lombok.Data;
import org.reseger.jrdbc.driver.connector.Connection;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.ResultSet;
import org.riseger.protoctl.search.command.USE;

@Data
public class SearchSession implements Session<SearchResponse> {
    private transient final Connection parent;
    private USE sql;

    public SearchSession(Connection parent) {
        this.parent = parent;
    }

    @Override
    public SearchResponse send() throws InterruptedException {
        SearchRequest message = new SearchRequest(sql);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return (SearchResponse) parent.getResult();
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
