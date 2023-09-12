package org.resegerdb.jrdbc.driver.session;

import org.resegerdb.jrdbc.command.search.command.SQL;
import org.resegerdb.jrdbc.command.search.command.USE;
import org.resegerdb.jrdbc.driver.connector.Connector;
import org.resegerdb.jrdbc.driver.result.Result;
import org.riseger.protoctl.message.PreloadDatabaseRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SearchSession implements Session {
    private transient final Connector parent;
    private List<SQL> sqlList = new LinkedList<>();

    public SearchSession(Connector parent) {
        this.parent = parent;
    }

    @Override
    public Result send() throws InterruptedException {
        PreloadDatabaseRequest message = new PreloadDatabaseRequest();
        System.out.println("发送到管道");
        parent.getChannel().writeAndFlush(message);
        return parent.getResult();
    }

    public void addSQL(SQL sql) {
        this.sqlList.add(sql);
    }

    public void addSQLs(SQL... sqls) {
        this.sqlList.addAll(Arrays.asList(sqls));
    }

    public void addSQLs(Collection<? extends SQL> sqls) {
        this.sqlList.addAll(sqls);
    }

    public USE use() {
        return new USE(this);
    }
}
