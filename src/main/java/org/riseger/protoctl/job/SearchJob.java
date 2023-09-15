package org.riseger.protoctl.job;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.main.search.SearchSession;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.search.command.USE;

import java.util.List;
import java.util.Map;

public class SearchJob implements Job {
    private static final Logger LOG = Logger.getLogger(SearchJob.class);

    private final TransponderHandler<SearchMessage, Map<String,List<Element_c>>> transponder;



    private final SearchSession session;

    public SearchJob(USE sql, TransponderHandler<SearchMessage, Map<String,List<Element_c>>> transponder) {
        this.session = new SearchSession(sql);
        this.transponder = transponder;
    }

    @Override
    public void run() {
        transponder.setE(session.process());
    }

}
