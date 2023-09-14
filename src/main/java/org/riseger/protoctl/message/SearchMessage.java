package org.riseger.protoctl.message;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.request.Request;
import org.riseger.protoctl.request.SearchRequest;
import org.riseger.protoctl.search.command.USE;

import java.util.List;

@Getter
@Setter
public class SearchMessage extends ResponseMessage {

    private final USE sql;

    public SearchMessage(USE sql) {
        super(MessageType.SEARCH);
        this.sql = sql;
    }

    @Override
    public Request warp() {
        return new SearchRequest(this.sql, (TransponderHandler<SearchMessage, List<Element_c>>) super.getTransponder());
    }

}
