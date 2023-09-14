package org.riseger.protoctl.message;

import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.request.PreloadDatabaseRequest;
import org.riseger.protoctl.request.Request;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class PreloadDatabaseMessage extends BasicMessage {

    private String uri;

    public PreloadDatabaseMessage() {
        super(MessageType.PRELOAD_DB);
    }

    @Override
    public Request warp() throws IOException {
        return JsonSerializer.deserialize(Utils.getText(new File(uri)).getBytes(StandardCharsets.UTF_8), PreloadDatabaseRequest.class);
    }

}
