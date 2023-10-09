package org.riseger.protoctl.packet.request;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.job.SearchJob;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.search.command.USE;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SearchRequest extends BasicRequest {

    private final USE sql;

    public SearchRequest(USE sql) {
        super(PacketType.SEARCH);
        this.sql = sql;
    }

    @Override
    public SearchJob warp() {
        return new SearchJob(this.sql, (TransponderHandler<SearchRequest, Map<String, List<Element_c>>>) super.getTransponder());
    }

}
