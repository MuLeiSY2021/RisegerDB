package org.riseger.protoctl.request;

import lombok.Data;
import org.riseger.protoctl.struct.model.Database;

import java.util.List;

@Data
public class PreloadRequest implements Request {
    private List<Database> databases;

    public PreloadRequest(List<Database> databases) {
        this.databases = databases;
    }
}
