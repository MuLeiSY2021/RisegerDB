package org.riseger.main.storage;

import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.cache.manager.ModelManager;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PreloadFSM {
    public static final Logger LOG = Logger.getLogger(PreloadFSM.class);
    public final String rootPath;

    public PreloadFSM(String rootPath) {
        if (rootPath.endsWith("/")) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }
        this.rootPath = rootPath;
    }

    private static void createLayer(File parent, Layer_c layer) {
        File layerFile = new File(parent.getPath() + "/" + layer.getName() + ".layer");
        Utils.writeToFile(layerToFile(layer), layerFile.getPath());
    }

    private static byte[] layerToFile(Layer_c layer) {
        try {
            RTree<MBRectangle_c> r_t = layer.getElementManager().getRtreeKeyIndex();
            ByteBuf buffer = r_t.serialize();
            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);
            return data;
        } catch (Throwable e) {
            LOG.error("Error", e);
        }
        return null;
    }

    private static void createConfig(File parent, Map<String, Config> configs) {
        File configFile = new File(parent.getPath() + "/" + "config" + ".json");
        Utils.writeToFile(configToFile(configs), configFile.getPath());
        LOG.info("创建Config文件成功");
    }

    private static byte[] configToFile(Map<String, Config> configs) {
        return JsonSerializer.serialize(configs);
    }

    private static void modelToFile(File parent, ModelManager models) {
        File modelFile = new File(parent.getPath() + "/" + "model" + ".json");
        Utils.writeToFile(modelsToFile(models), modelFile.getPath());
        LOG.info("创建Model文件成功");
    }

    private static byte[] modelsToFile(ModelManager models) {
        return JsonSerializer.serialize(models);
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

    public void saveDatabase(Database_c database) throws IOException {
        File root = new File(rootPath + "/data/databases");
        try {
            //创建数据库根目录
            String res = createDir(root);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库根目录");
                } else {
                    LOG.info("创建数据库根目录成功");
                }
            }

            //创建数据库目录
            File db = new File(root.getPath() + "/" + database.getName() + ".db");
            res = createDir(db);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "数据库目录");
                } else {
                    LOG.info("创建数据库目录成功");
                }
            }

            //创建Config文件
            createConfig(db, database.getConfigs());

            //创建Model文件
            modelToFile(db, database.getModelManager());

            //创建Map文件
            for (MapDB_c map : database.getMapDBManager().toList()) {
                createMap(db, map, null);
            }

        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
    }

    private void createSubmap(MapDB_c map, File mapFile) throws IOException {
        String res;
        File submapFile = new File(mapFile.getPath() + "/" + map.getName() + "." + Constant.SUBMAP_PREFIX);
        res = createDir(submapFile);
        if (res != null) {
            if (!res.isEmpty()) {
                LOG.error(res + "地图目录");
            } else {
                LOG.info("创建地图目录成功");
            }
        }
        createMap(submapFile, map, submapFile);
    }

    private void createMap(File parent, MapDB_c map, File rootFile) throws IOException {
        if (rootFile == null) {
            rootFile = new File(parent.getPath() + "/" + map.getName() + ".mp");
            String res = createDir(rootFile);
            if (res != null) {
                if (!res.isEmpty()) {
                    LOG.error(res + "地图目录");
                } else {
                    LOG.info("创建地图目录成功");
                }
            }
        }

        //创建Config文件
        createConfig(rootFile, map.getConfigs());

        //创建Layer文件夹
        for (Layer_c layer : map.getLayers().toList()) {
            if (layer.isSubMap()) {
                rootFile = new File(rootFile.getPath() + "/" + layer.getName() + ".layer");
                String res = createDir(rootFile);
                if (res != null) {
                    if (!res.isEmpty()) {
                        LOG.error(res + "地图目录");
                    } else {
                        LOG.info("创建地图目录成功");
                    }
                }
                List<MBRectangle_c> maps = layer.getElementManager().getRtreeKeyIndex().getElements();
                for (MBRectangle mbr : maps) {
                    createSubmap((MapDB_c) mbr, rootFile);
                }
            } else {
                createLayer(rootFile, layer);
            }
        }
    }
}
