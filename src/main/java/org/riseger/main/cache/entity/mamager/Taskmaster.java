package org.riseger.main.cache.entity.mamager;

import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.api.workflow.workflow.CommonWorkFlow;
import org.riseger.main.cache.entity.element.Model_c;
import org.riseger.main.cache.entity.mamager.dao.DAO;
import org.riseger.main.cache.entity.mamager.dao.request.PreloadDBCMRequest;
import org.riseger.main.cache.entity.element.Database_c;
import org.riseger.main.cache.entity.element.Scope_c;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.config.Option;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.protoctl.struct.entity.Type;

import java.util.Map;

public class Taskmaster implements DAO {
    private CommonWorkFlow workFlow = new CommonWorkFlow();

    @Override
    public Database_c createDatabase(String name, Map<Config, String> configs) {

        return null;
    }

    @Override
    public void preloadDatabase(Database database, revocable revocable) {
        workFlow.push(new PreloadDBCMRequest(database, revocable));
    }

    @Override
    public Model_c createModel(Database_c databaseC, String name, String parent, Map<String, Type> parameters, Map<Option, String> configs) {
        return null;
    }
}
