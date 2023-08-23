package org.riseger.main.cache.entity.manager.dao.job;

import org.riseger.main.api.workflow.job.Job;
import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.cache.entity.element.Database_c;
import org.riseger.main.cache.entity.manager.CacheMaster;
import org.riseger.protoctl.struct.entity.Database;

public class PreloadDBCMJob implements Job {
    private final Database database;

    private final revocable<Database_c> revocable;

    public PreloadDBCMJob(Database database, revocable<Database_c> revocable) {
        this.database = database;
        this.revocable = revocable;
    }

    @Override
    public void run() {
        revocable.setE(CacheMaster.INSTANCE.getDatabaseManager().preloadDatabase(database));
    }
}
