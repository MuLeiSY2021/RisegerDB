package org.riseger.main.cache.manager.dao;

import org.riseger.main.api.workflow.revoke.revocable;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.Model_c;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.config.Option;
import org.riseger.protoctl.struct.entity.Database;
import org.riseger.protoctl.struct.entity.Type;

import java.util.Map;

public interface DAO {
    Database_c createDatabase(String name, Map<Config, String> configs);

    void preloadDatabase(Database database, revocable preloadJob);

    Model_c createModel(Database_c databaseC, String name, String parent, Map<String, Type> parameters, Map<Option, String> configs);

}
