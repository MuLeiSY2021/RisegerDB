package org.riseger.main.system;

import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.builder.MapInitBuilder;
import org.riseger.main.system.cache.component.*;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ModelManager;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StorageSystem {

    public static final Logger LOG = Logger.getLogger(StorageSystem.class);

    public static StorageSystem DEFAULT;

    public final String rootPath;

    public StorageSystem(String rootPath) {
        this.rootPath = rootPath;
    }


    public List<Database_c> initDatabases() throws IOException {
        List<Database_c> databases = new LinkedList<>();
        File root = new File(this.rootPath + "/data/databases");

        if (root.exists()) {
            for (File db_ : root.listFiles()) {
                if (db_.isDirectory() && db_.getName().endsWith(Constant.DATABASE_PREFIX)) {
                    databases.add(getDatabase(db_));
                }
            }
        }
        return databases;
    }

    private static void writeLayer(File parent, Layer_c layer) {
        File layerFile = new File(parent.getPath() + "/" + layer.getName() + ".layer");
        byte[] data = null;
        try {
            RTree<MBRectangle_c> r_t = layer.getElementManager().getRtreeKeyIndex();
            ByteBuf buffer = r_t.serialize();
            data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);
        } catch (Throwable e) {
            LOG.error("Error", e);
        }
        Utils.writeToFile(data, layerFile.getPath());
    }

    private static void writeModel(File parent, ModelManager models) {
        File modelFile = new File(parent.getPath() + "/" + Constant.MODEL_FILE_NAME + Constant.DOT_PREFIX + Constant.JSON_PREFIX);
        Utils.writeToFile(JsonSerializer.serialize(models), modelFile.getPath());
        LOG.info("创建Model文件成功");
    }

    private Database_c getDatabase(File db_) throws IOException {
        Database_c db = new Database_c(db_.getName().split("\\.")[0]);
        for (File map_ : db_.listFiles()) {
            if (map_.isDirectory() && map_.getName().endsWith(Constant.MAP_PREFIX)) {
                db.initMap(getMap(map_, db));
            } else if (map_.getName().startsWith(Constant.CONFIG_FILE_NAME)) {
                ConfigManager configs = JsonSerializer.deserialize(Utils.getText(map_).getBytes(StandardCharsets.UTF_8), ConfigManager.class);
                db.setConfigManager(configs);
            } else if (map_.getName().startsWith(Constant.MODEL_FILE_NAME)) {
                db.setModelManager(JsonSerializer.deserialize(Utils.getBytes(map_), ModelManager.class));
            }
        }
        return db;
    }

    private Map_c getMap(File map_, Database_c db) {
        MapInitBuilder mb = new MapInitBuilder();
        mb.setDatabase(db);
        mb.setName(map_.getName().split("\\.")[0]);
        for (File submap_ : Objects.requireNonNull(map_.listFiles())) {
            if (submap_.getName().endsWith(Constant.LAYER_PREFIX)) {
                mb.addLayer(submap_);
            } else if (submap_.getName().startsWith(Constant.CONFIG_FILE_NAME)) {
                Map<String, Config_c> configs = (Map<String, Config_c>) JsonSerializer.deserialize(Utils.getText(submap_),
                        TypeToken.getParameterized(HashMap.class, String.class, Config_c.class));
                mb.setConfigs(configs);
            }
        }

        return mb.build();
    }

    public void writeDatabases(List<Database_c> databases) {
        for (Database_c db : databases) {
            try {
                writeDatabase(db);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeDatabase(Database_c database) throws IOException {
        File root = new File(rootPath + "/data/databases");
        try {
            //创建数据库根目录
            String res = writeDir(root);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库根目录");
                } else {
                    LOG.info("创建数据库根目录成功");
                }
            }

            //创建数据库目录
            File db = new File(root.getPath() + "/" + database.getName() + ".db");
            res = writeDir(db);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库目录");
                } else {
                    LOG.info("创建数据库目录成功");
                }
            }

            //创建Config文件
            writeConfig(db, database.getConfigManager());

            //创建Model文件
            writeModel(db, database.getModelManager());

            //创建Map文件
            for (Map_c map : database.getMapManager().toList()) {
                writeMap(db, map, null);
            }

        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    private void writeMap(File parent, Map_c map, File rootFile) throws IOException {
        if (rootFile == null) {
            rootFile = new File(parent.getPath() + "/" + map.getName() + ".mp");
            String res = writeDir(rootFile);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "地图目录");
                } else {
                    LOG.info("创建地图目录成功");
                }
            }
        }

        //创建Config文件
        writeConfig(rootFile, map.getConfigManager());

        //创建Layer文件夹
        for (Layer_c layer : map.getLayerManager().toList()) {
            if (layer.isSubMap()) {
                rootFile = new File(rootFile.getPath() + "/" + layer.getName() + ".layer");
                String res = writeDir(rootFile);
                if (res != null) {
                    if (!res.isEmpty()) {
                        LOG.error(res + "地图目录");
                    } else {
                        LOG.info("创建地图目录成功");
                    }
                }
                List<MBRectangle_c> maps = layer.getElementManager().getRtreeKeyIndex().getElements();
                for (MBRectangle mbr : maps) {
                    File submapFile = new File(rootFile.getPath() + "/" + map.getName() + "." + Constant.SUBMAP_PREFIX);
                    writeDir(submapFile);
                    writeMap(submapFile, (Map_c) mbr, submapFile);
                }
            } else {
                writeLayer(rootFile, layer);
            }
        }
    }

    public String writeDir(File file) {
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdirs()) {
                if (!file.exists()) {
                    if (!file.isDirectory()) {
                        return "已存在同名文件，无法创建";
                    } else {
                        return "权限不足，无法创建";
                    }
                }
                return "";
            }
        }
        return null;
    }

    private void writeConfig(File parent, ConfigManager configManager) {
        File configFile = new File(parent.getPath() + "/" + "config" + ".json");
        Utils.writeToFile(JsonSerializer.serialize(configManager), configFile.getPath());
        LOG.info("创建Config文件成功");
    }

    public void organizeDatabases(List<Database_c> databases) throws IOException {
        for (Database_c db : databases) {
            if (db.isChanged()) {
                organizeDatabase(db);
                db.resetChanged();
            }
        }
    }

    private void organizeDatabase(Database_c database) throws IOException {
        File root = new File(rootPath + "/data/databases");
        File databaseFile = new File(root.getPath() + "/" + database.getName() + ".db");
        if (!databaseFile.exists()) {
            writeDatabase(database);
            return;
        }

        //Config Update
        organizeConfig(database.getConfigManager(), databaseFile);

        //Model Update
        ModelManager modelManager = database.getModelManager();
        if (modelManager.isChanged()) {
            writeModel(databaseFile, modelManager);
            modelManager.resetChanged();
        }

        //Map Update
        if (database.getMapManager().isChanged()) {
            for (Map_c map : database.getMapManager().getMaps()) {
                if (map.isChanged()) {
                    organizeMap(root, map, null);
                    map.resetChanged();
                }
            }
            database.getMapManager().resetChanged();
        }

    }

    private void organizeConfig(ConfigManager configManager, File databaseFile) {
        if (configManager.isChanged()) {
            writeConfig(databaseFile, configManager);
            configManager.resetChanged();
        }
    }

    private void organizeMap(File parent, Map_c map, File rootFile) {
        //Config Update
        organizeConfig(map.getConfigManager(), parent);


        if (map.getLayerManager().isChanged()) {
            for (Layer_c layer : map.getLayerManager().toList()) {
                if (layer.isChanged()) {
                    if (layer.isSubMap()) {
                        rootFile = new File(rootFile.getPath() + "/" + layer.getName() + ".layer");
                        List<MBRectangle_c> maps = layer.getElementManager().getRtreeKeyIndex().getElements();
                        for (MBRectangle mbr : maps) {
                            organizeMap(new File(rootFile.getPath() + "/" + map.getName() + "." + Constant.SUBMAP_PREFIX), (Map_c) mbr, rootFile);
                        }
                    } else {
                        writeLayer(rootFile, layer);
                    }
                    layer.resetChanged();
                }
            }
            map.getLayerManager().resetChanged();
        }
    }
}
