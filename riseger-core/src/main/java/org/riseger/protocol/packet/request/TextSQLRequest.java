package org.riseger.protocol.packet.request;

import lombok.Setter;
import org.riseger.protocol.job.Job;
import org.riseger.protocol.job.TextSQLJob;
import org.riseger.protocol.packet.PacketType;
import org.riseger.protocol.packet.RequestType;

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
