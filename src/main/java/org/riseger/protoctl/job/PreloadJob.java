package org.riseger.protoctl.job;

import org.apache.logging.log4j.LogManager;
import org.riseger.main.api.workflow.revoke.Revocable;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.DBCacheManager;
import org.riseger.protoctl.struct.entity.Database;

import java.util.List;

public class PreloadJob extends Revocable<Database_c> implements Job {
    private final List<Database> databases;

    private Database_c database_c;

    public PreloadJob(List<Database> databases) {
        this.databases = databases;
    }

    @Override
    public void run() {
        for (Database database : databases) {
            DBCacheManager.DAO.preloadDatabase(database, this);

            try {
                super.sleep();
            } catch (InterruptedException e) {
                LogManager.getLogger(this.getClass()).error(e.getMessage());
            }
        }
    }

    @Override
    public Database_c getE() {
        return database_c;
    }

    @Override
    public void setE(Database_c database_c) {
        this.database_c = database_c;

        super.wake();
    }

}
