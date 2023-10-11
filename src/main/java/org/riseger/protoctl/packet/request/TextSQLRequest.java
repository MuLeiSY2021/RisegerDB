package org.riseger.protoctl.packet.request;

import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.TextSQLJob;
import org.riseger.protoctl.packet.PacketType;

public class TextSQLRequest extends TranspondRequest {
    private final String text;

    public TextSQLRequest(String text) {
        super(PacketType.TEXT_SQL);
        this.text = text;
    }

    @Override
    public Job warp() {
        return new TextSQLJob(super.getTransponder(), text);
    }
}
