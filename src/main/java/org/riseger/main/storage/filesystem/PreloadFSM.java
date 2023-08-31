package org.riseger.main.storage.filesystem;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.main.cache.manager.ModelManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.Map;

public class PreloadFSM {
    public String rootPath;

    public static final Logger LOG = Logger.getLogger(PreloadFSM.class);

    public PreloadFSM(String rootPath) {
        if(rootPath.endsWith("/")) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        this.rootPath = rootPath;
    }

    public void saveDatabase(Database_c database) {
        File root = new File(rootPath + "/data/databases");
        try {
            //创建数据库根目录
            String res = createDir(root);
            if(res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库根目录");
                } else {
                    LOG.info("创建数据库根目录成功");
                }
            }

            //创建数据库目录
            File db = new File(root.getPath() + "/" + database.getName() + ".db");
            res = createDir(db);
            if(res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库目录");
                } else {
                    LOG.info("创建数据库目录成功");
                }
            }

            //创建Config文件
            createConfig(db, database.getConfigs());

            //创建Model文件
            modelToFile(db, database.getModels());

            //创建Map文件
            for (MapDB_c map :database.getMaps().toList()) {
                File mapFile = new File(db.getPath() + "/" + map.getName() + ".mp");
                res = createDir(mapFile);
                if(res != null) {
                    if (!res.isEmpty()) {
                        LOG.error(res + "地图目录");
                    } else {
                        LOG.info("创建地图目录成功");
                    }
                }

                //创建Config文件
                createConfig(mapFile, map.getConfigs());

                //创建Layer文件
                for (Layer_c layer:map.getLayers().toList()) {

                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    private static File createLayer(File parent, Layer_c layer) {
        File layerFile = new File(parent.getPath() + "/" + layer.getName() + ".layer");
        Utils.writeToFile(layerToFile(layer), layerFile.getPath());
        return layerFile;
    }

    private static byte[] layerToFile(Layer_c layer) {
        layer.getElementManager().getRtreeKeyIndex();
        return null;
    }

    private static File createConfig(File parent, Map<String, Config> configs) {
        File configFile = new File(parent.getPath() + "/" + "config" + ".json");
        Utils.writeToFile(configToFile(configs), configFile.getPath());
        LOG.info("创建Config文件成功");
        return configFile;
    }

    private static String configToFile(Map<String,Config> configs) {
        return Utils.toJson(configs);
    }

    private static File modelToFile(File parent, ModelManager models) {
        File modelFile = new File(parent.getPath() + "/" + "model" + ".json");
        Utils.writeToFile(modelsToFile(models), modelFile.getPath());
        LOG.info("创建Model文件成功");
        return modelFile;
    }

    private static String modelsToFile(ModelManager models) {
        return Utils.toJson(models);
    }

    private static String createDir(File file) {
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
}
