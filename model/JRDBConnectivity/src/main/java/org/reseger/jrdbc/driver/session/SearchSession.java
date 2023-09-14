package org.reseger.jrdbc.driver.session;

import lombok.Data;
import org.reseger.jrdbc.driver.connector.Connector;
import org.reseger.jrdbc.driver.result.Result;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.search.command.USE;

@Data
public class SearchSession implements Session {
    private transient final Connector parent;
    private USE sql;

    public SearchSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public Result send() throws InterruptedException {
        SearchMessage message = new SearchMessage(sql);
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return parent.getResult();
    }

    public USE use() {
        sql = new USE();
        return sql;
    }
}
