package org.riseger.main.system;

import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.builder.MapInitBuilder;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.cache.component.Layer;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ModelManager;
import org.riseger.protocol.serializer.JsonSerializer;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RTree;

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


    private static void writeLayer(File parent, Layer layer) throws Throwable {
        File layerFile = new File(parent.getPath() + "/" + layer.getName() + ".layer");
        byte[] data;
        RTree<GeoRectangle> r_t = layer.getElementManager().getRtreeKeyIndex();
        ByteBuf buffer = r_t.serialize();
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        Utils.writeToFile(data, layerFile.getPath());
    }

    public List<Database> initDatabases() throws IOException {
        List<Database> databases = new LinkedList<>();
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

    private static void writeModel(File parent, ModelManager models) {
        File modelFile = new File(parent.getPath() + "/" + Constant.MODEL_FILE_NAME + Constant.DOT_PREFIX + Constant.JSON_PREFIX);
        Utils.writeToFile(JsonSerializer.serialize(models), modelFile.getPath());
        LOG.info("创建Model文件成功");
    }

    private Database getDatabase(File db_) throws IOException {
        Database db = new Database(db_.getName().split("\\.")[0]);
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

    private GeoMap getMap(File map_, Database db) throws IOException {
        MapInitBuilder mb = new MapInitBuilder();
        mb.setDatabase(db);
        mb.setName(map_.getName().split("\\.")[0]);
        for (File submap_ : Objects.requireNonNull(map_.listFiles())) {
            if (submap_.getName().endsWith(Constant.LAYER_PREFIX)) {
                mb.addLayer(submap_);
            } else if (submap_.getName().startsWith(Constant.CONFIG_FILE_NAME)) {
                ConfigManager configs = JsonSerializer.deserialize(Utils.getBytes(submap_),
                        ConfigManager.class);
                mb.setConfigs(configs);
            }
        }

        return mb.build();
    }

    public void writeDatabases(List<Database> databases) throws Throwable {
        for (Database db : databases) {
            writeDatabase(db);
        }
    }

    public void writeDatabase(Database database) throws Throwable {
        File root = new File(rootPath + "/data/databases");
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
        for (GeoMap map : database.getMapManager().toList()) {
            writeMap(db, map, null);
        }
    }

    private void writeMap(File parent, GeoMap map, File rootFile) throws Throwable {
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
        for (Layer layer : map.getLayerManager().toList()) {
            writeLayer(rootFile, layer);
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

    public void organizeDatabases(List<Database> databases) throws Throwable {
        for (Database db : databases) {
            if (db.isChanged()) {
                organizeDatabase(db);
                db.resetChanged();
            }
        }
    }

    private void organizeDatabase(Database database) throws Throwable {
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
            for (GeoMap map : database.getMapManager().getMaps()) {
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

    private void organizeMap(File parent, GeoMap map, File rootFile) throws Throwable {
        //Config Update
        organizeConfig(map.getConfigManager(), parent);


        if (map.getLayerManager().isChanged()) {
            for (Layer layer : map.getLayerManager().toList()) {
                if (layer.isChanged()) {
                    writeLayer(rootFile, layer);
                    layer.resetChanged();
                }
            }
            map.getLayerManager().resetChanged();
        }
    }

    public File[] getDatabasesFile() {
        File root = new File(this.rootPath + "/data/databases");
        return root.listFiles();
    }

    public List<File> getLogFiles(File database, boolean sort) {
        List<File> logFiles = new LinkedList<>();
        File logsDir = new File(database.getPath() + "/logs");
        if (logsDir.exists()) {
            logFiles.addAll(Arrays.asList(Objects.requireNonNull(logsDir.listFiles())));
            if (sort) {
                logFiles.sort(Comparator.comparing(File::getName));
            }
        }

        return logFiles;
    }
}
