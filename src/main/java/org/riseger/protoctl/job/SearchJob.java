package org.riseger.protoctl.job;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.main.sql.search.SearchSession;
import org.riseger.protoctl.packet.request.SearchRequest;
import org.riseger.protoctl.search.command.USE;

import java.util.List;
import java.util.Map;

public class SearchJob implements Job {
    private static final Logger LOG = Logger.getLogger(SearchJob.class);

    private final TransponderHandler<SearchRequest, Map<String, List<Element_c>>> transponder;



    private SearchSession session;

    public SearchJob(USE sql, TransponderHandler<SearchRequest, Map<String, List<Element_c>>> transponder) {
        this.transponder = transponder;
        try {
            this.session = new SearchSession(sql);
        } catch (Exception e) {
            transponder.send(e);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            transponder.setE(session.process());
        } catch (Exception e) {
            transponder.send(e);
            e.printStackTrace();
        }
    }

}
