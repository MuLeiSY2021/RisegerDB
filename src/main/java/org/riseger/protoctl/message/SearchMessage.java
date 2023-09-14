package org.riseger.protoctl.message;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.request.Request;
import org.riseger.protoctl.request.SearchRequest;
import org.riseger.protoctl.search.command.USE;

@Getter
@Setter
public class SearchMessage extends BasicMessage {

    private final USE sql;

    public SearchMessage(USE sql) {
        super(MessageType.SEARCH);
        this.sql = sql;
    }

    @Override
    public Request warp() {
        return new SearchRequest(this.sql);
    }
}
