package org.riseger.protoctl.packet.request;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.PreloadJob;
import org.riseger.protoctl.packet.PacketType;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.utils.Utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

@Getter
@Setter
public class PreloadRequest extends BasicRequest {

    private String uri;

    public PreloadRequest(String uri) {
        super(PacketType.PRELOAD_DB);
        this.uri = uri;
    }

    @Override
    public Job warp() {
        return new PreloadJob(
                JsonSerializer.deserialize(Utils.getText(new File(uri)).getBytes(StandardCharsets.UTF_8),
                        new TypeToken<LinkedList<Database>>() {
                        }.getType()));
    }

}
