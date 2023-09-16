package org.reseger.jrdbc.driver.session;

import lombok.Data;
import org.reseger.jrdbc.driver.connector.Connector;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.command.USE;

@Data
public class SearchSession implements Session<SearchResponse> {
    private transient final Connector parent;
    private USE sql;

    public SearchSession(Connector parent) {
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
}
