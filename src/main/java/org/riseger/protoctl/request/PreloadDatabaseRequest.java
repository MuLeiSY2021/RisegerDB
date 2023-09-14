package org.riseger.protoctl.request;

import lombok.Data;
import org.riseger.protoctl.job.PreloadJob;
import org.riseger.protoctl.struct.entity.Database;

import java.util.List;

@Data
public class PreloadDatabaseRequest implements Request {
    private List<Database> databases;

    public PreloadDatabaseRequest(List<Database> databases) {
        this.databases = databases;
    }

    @Override
    public PreloadJob warp() {
        return new PreloadJob(this.databases);
    }
}
