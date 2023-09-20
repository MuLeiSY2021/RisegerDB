package org.riseger.protoctl.job;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.workflow.revoke.Revocable;
import org.riseger.protoctl.struct.entity.Database;

import java.io.IOException;
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
            try {
                CacheMaster.INSTANCE.getDatabaseManager().preloadDatabase(database);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
