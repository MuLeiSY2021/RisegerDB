package org.riseger.main.cache.manager.dao.request;

import lombok.Data;
import org.riseger.main.api.workflow.revoke.Revocable;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.dao.job.PreloadDBCMJob;
import org.riseger.protoctl.job.Job;
import org.riseger.protoctl.request.Request;
import org.riseger.protoctl.struct.entity.Database;

@Data
public class PreloadDBCMRequest implements Request {
    private Database database;

    private Revocable<?> revocable;

    public PreloadDBCMRequest(Database database, Revocable<?> revocable) {
        this.database = database;
        this.revocable = revocable;
    }

    @Override
    public Job warp() {
        return new PreloadDBCMJob(this.database, (Revocable<Database_c>) this.revocable);
    }
}
