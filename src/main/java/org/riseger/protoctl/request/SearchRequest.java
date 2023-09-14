package org.riseger.protoctl.request;

import lombok.Data;
import org.riseger.protoctl.job.SearchJob;
import org.riseger.protoctl.search.command.USE;

@Data
public class SearchRequest implements Request {
    private final USE sql;

    public SearchRequest(USE sql) {
        this.sql = sql;
    }

    @Override
    public SearchJob warp() {
        return new SearchJob(this.sql);
    }
}
