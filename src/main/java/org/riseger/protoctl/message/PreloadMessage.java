package org.riseger.protoctl.message;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.job.PreloadJob;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.utils.Utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
@Setter
public class PreloadMessage extends BasicMessage {

    private String uri;

    public PreloadMessage() {
        super(PreloadMessage.class);
    }

    @Override
    public Job warp() {
        return new PreloadJob(
                JsonSerializer.deserialize(Utils.getText(new File(uri))
                                .getBytes(StandardCharsets.UTF_8),
                        new TypeToken<List<Database>>() {
                        }.getType()));
    }

}
