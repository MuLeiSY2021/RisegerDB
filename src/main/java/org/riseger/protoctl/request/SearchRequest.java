package org.riseger.protoctl.request;

import lombok.Data;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.job.SearchJob;
import org.riseger.protoctl.message.SearchMessage;
import org.riseger.protoctl.search.command.USE;

import java.util.List;
import java.util.Map;

@Data
public class SearchRequest implements Request {
    private final USE sql;

    private final TransponderHandler<SearchMessage, Map<String,List<Element_c>>> transponder;

    public SearchRequest(USE sql, TransponderHandler<SearchMessage, Map<String,List<Element_c>>> transponder) {
        this.transponder = transponder;
        this.sql = sql;
    }

    @Override
    public SearchJob warp() {
        return new SearchJob(this.sql, this.transponder);
    }
}
