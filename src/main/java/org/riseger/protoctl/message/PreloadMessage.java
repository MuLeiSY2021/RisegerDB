package org.riseger.protoctl.message;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.riseger.protoctl.request.PreloadRequest;
import org.riseger.protoctl.request.Request;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class PreloadMessage extends BasicMessage {

    private String uri;

    public PreloadMessage() {
        super(MessageType.TYPE_0_PRELOAD);
    }

    @Override
    public Request warp() {
        return new PreloadRequest((List<Database>) Utils.toJson(Utils.getText(new File(uri)), TypeToken.getParameterized(List.class,Database.class)));
    }

}
