package org.riseger.protoctl.packet.request;

import org.riseger.main.entry.handler.TransponderHandler;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.TextSQLJob;
import org.riseger.protoctl.packet.PacketType;

public class TextSQLRequest extends BasicRequest {
    private final String text;

    public TextSQLRequest(String text) {
        super(PacketType.TEXT_SQL);
        this.text = text;
    }

    @Override
    public Job warp() {
        return new TextSQLJob(text, (TransponderHandler<SearchRequest, String>) super.getTransponder());
    }
}
