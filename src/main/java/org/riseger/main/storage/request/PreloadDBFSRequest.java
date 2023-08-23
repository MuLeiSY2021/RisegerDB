package org.riseger.main.storage.request;

import lombok.Data;
import org.riseger.main.api.workflow.job.Job;
import org.riseger.main.cache.entity.element.Database_c;
import org.riseger.main.storage.job.PreloadDBFSJob;
import org.riseger.protoctl.request.Request;

@Data
public class PreloadDBFSRequest implements Request {
    private Database_c database;

    public PreloadDBFSRequest(Database_c database) {
        this.database = database;
    }

    @Override
    public Job warp() {
        return new PreloadDBFSJob(this.database);
    }
}
