package org.riseger.protoctl.packet.request;

import lombok.Setter;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.TextSQLJob;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.packet.RequestType;

@Setter
public class TextSQLRequest extends TranspondRequest {
    private final String text;

    private String ipAddress;

    public TextSQLRequest(String text, RequestType type, String ipAddress) {
        super(PacketType.TEXT_SQL, type);
        this.text = text;
        this.ipAddress = ipAddress;
    }

    @Override
    public Job warp() {
        return new TextSQLJob(super.getTransponder(), text, ipAddress);
    }
}
