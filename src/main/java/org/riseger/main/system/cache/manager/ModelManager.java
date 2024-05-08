package org.riseger.main.system.cache.manager;

import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.HolisticStorageEntity_i;
import org.riseger.main.system.cache.LockableEntity;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.Model;
import org.riseger.protocol.struct.entity.Model_p;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelManager extends LockableEntity implements HolisticStorageEntity_i {
    private transient final Database database;

    private final HolisticStorageEntity entity = new HolisticStorageEntity();

    private final Map<String, Model> models = new ConcurrentHashMap<>();

    public ModelManager(Database database) {
        this.database = database;
    }

    public void addModel(Model_p model) {
        super.write();
        models.put(model.getName(), new Model(model));
        super.unwrite();
    }

    public Model getModel(String name) {
        super.read();
        Model model = models.get(name);
        super.unread();
        return model;
    }

    public List<Model> getModels() {
        super.read();
        List<Model> modelList = new LinkedList<>(models.values());
        super.unread();
        return modelList;
    }

    @Override
    public void changeEntity() {
        entity.changeEntity();
    }

    @Override
    public void resetChanged() {
        entity.resetChanged();
    }

    @Override
    public boolean isChanged() {
        return entity.isChanged();
    }
}
