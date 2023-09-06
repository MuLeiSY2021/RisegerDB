package org.riseger.main.cache.manager.dao.job;

import org.riseger.main.api.workflow.job.Job;
import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.protoctl.struct.entity.Database;

import java.io.IOException;

public class PreloadDBCMJob implements Job {
    private final Database database;

    private final revocable<Database_c> revocable;

    public PreloadDBCMJob(Database database, revocable<Database_c> revocable) {
        this.database = database;
        this.revocable = revocable;
    }

    @Override
    public void run() {
        try {
            revocable.setE(CacheMaster.INSTANCE.getDatabaseManager().preloadDatabase(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
