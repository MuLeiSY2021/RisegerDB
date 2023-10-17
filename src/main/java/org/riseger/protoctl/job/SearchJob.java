package org.riseger.protoctl.job;

import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.main.sql.search.SearchSession;
import org.riseger.protoctl.packet.request.TranspondRequest;
import org.riseger.protoctl.packet.response.SearchResponse;
import org.riseger.protoctl.search.command.USE;

public class SearchJob extends TranspondJob {

    private USE sql;

    public SearchJob(USE sql, TransponderHandler<? extends TranspondRequest> transponder) {
        super(transponder);
        this.sql = sql;
    }

    @Override
    public void process() throws Exception {
        SearchResponse response = new SearchResponse();
        super.prepare(response);

        SearchSession session = new SearchSession(sql, database, scope, models, searches, memory);
        this.sql = null;
        response.setResultMap(session.process());

        super.done();
    }

}
