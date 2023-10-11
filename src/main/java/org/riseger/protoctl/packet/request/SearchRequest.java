package org.riseger.protoctl.packet.request;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.job.SearchJob;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.search.command.USE;

@Getter
@Setter
public class SearchRequest extends TranspondRequest {

    private final USE sql;

    public SearchRequest(USE sql) {
        super(PacketType.SEARCH);
        this.sql = sql;
    }

    @Override
    public SearchJob warp() {
        return new SearchJob(this.sql, (TransponderHandler<SearchRequest>) super.getTransponder());
    }

}
