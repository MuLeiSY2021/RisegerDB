package org.riseger.main.storage.job;

import org.riseger.main.api.workflow.job.Job;
import org.riseger.main.cache.entity.component.db.Database_c;

public class PreloadDBFSJob implements Job {
    private final Database_c database;


    public PreloadDBFSJob(Database_c database) {
        this.database = database;
    }

    //TODO:实现对整个数据库的完全序列化
    @Override
    public void run() {
    }
}
