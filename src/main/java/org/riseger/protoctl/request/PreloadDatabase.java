package org.riseger.protoctl.request;

import lombok.Data;
import org.riseger.main.api.workflow.job.PreloadJob;
import org.riseger.protoctl.struct.entity.Database;

import java.util.List;

@Data
public class PreloadDatabase implements Request {
    private List<Database> databases;

    public PreloadDatabase(List<Database> databases) {
        this.databases = databases;
    }

    @Override
    public PreloadJob warp() {
        return new PreloadJob(this.databases);
    }
}
