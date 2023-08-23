package org.riseger.main.api.workflow.job;

import org.apache.logging.log4j.LogManager;
import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.cache.entity.element.Database_c;
import org.riseger.main.cache.entity.manager.DBCacheManager;
import org.riseger.protoctl.struct.entity.Database;

import java.util.List;

public class PreloadJob extends revocable<Database_c> implements Job {
    private final List<Database> databases;

    private Database_c database_c;

    public PreloadJob(List<Database> databases) {
        this.databases = databases;
    }

    @Override
    public void run() {
        for (Database database : databases) {
            DBCacheManager.DAO.preloadDatabase(database,this);

            //TODO:考虑异步任务发布
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

//    private void addModel(Database_c databaseC, List<Model> models) {
//        for (Model model:models) {
//            Model_c child_c = DBCacheManager.DAO.createModel(
//                    databaseC,
//                    model.getName(),
//                    model.getParent(),
//                    model.getParameters(),
//                    model.getConfigs()
//            );
//        }
//
//    }
//
//    private void addScope(Database_c database_c,List<Scope> parents) {
//        for (Scope parent:parents) {
//            if(parent.getChildren() == null ||parent.getChildren().size() <= 0) {
//                return;
//            }
//            for (Scope scope : parent.getChildren()) {
//                Scope_c scope_c = DBCacheManager.DAO.createScope(database_c, scope.getName());
//                addScope(database_c,scope_c, scope);
//            }
//        }
//
//    }
//
//    private void addScope(Database_c database_c,Scope_c parent_c, Scope parent) {
//        if(parent.getChildren() == null ||parent.getChildren().size() <= 0) {
//            return;
//        }
//        for (Scope scope : parent.getChildren()) {
//            Scope_c scope_c = DBCacheManager.DAO.createScope(database_c, scope.getName(), parent_c);
//            addScope(database_c,scope_c, scope);
//        }
//    }
}
