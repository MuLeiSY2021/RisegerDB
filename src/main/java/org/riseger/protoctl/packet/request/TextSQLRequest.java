package org.riseger.protoctl.packet.request;

import lombok.Setter;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.TextSQLJob;
import org.riseger.protoctl.packet.PacketType;

@Setter
public class TextSQLRequest extends TranspondRequest {
    private final String text;

    private String ipAddress;

    public TextSQLRequest(String text) {
        super(PacketType.TEXT_SQL);
        this.text = text;
    }

    @Override
    public Job warp() {
        return new TextSQLJob(super.getTransponder(), text, ipAddress);
    }
}
