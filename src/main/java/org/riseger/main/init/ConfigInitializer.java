package org.riseger.main.init;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.riseger.main.constant.Config;
import org.riseger.utils.Utils;

import java.io.File;

public class ConfigInitializer extends Initializer {
    private static final Logger LOG = Logger.getLogger(ConfigInitializer.class);

    public ConfigInitializer(String rootPath) {
        super(rootPath);
    }

    @Override
    public boolean init() throws Exception {
        LOG.info("Set working directory..");
        File workdir = new File(rootPath + "/configs");

        File xmlConfigFile = Utils.getFile(workdir, "config.xml");

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(xmlConfigFile);
            Element xmlConfig = document.getRootElement();

            Config.PORT = Integer.parseInt(xmlConfig.elementText("port"));
            LOG.debug("Config Set port " + Config.PORT);

            Config.NETTY_BLOCKING_LOG = Integer.parseInt(xmlConfig.elementText("blocking-log"));
            LOG.debug("Config Set blocking-log " + Config.NETTY_BLOCKING_LOG);

            Config.CORE_POOL_SIZE = Integer.parseInt(xmlConfig.elementText("core-pool-size"));
            LOG.debug("Config Set core-pool-size " + Config.CORE_POOL_SIZE);

            Config.MAX_POOL_SIZE = Integer.parseInt(xmlConfig.elementText("max-pool-size"));
            LOG.debug("Config Set max-pool-size " + Config.MAX_POOL_SIZE);

            Config.KEEP_ALIVE_TIME = Integer.parseInt(xmlConfig.elementText("keep-alive-time"));
            LOG.debug("Config Set keep-alive-time " + Config.KEEP_ALIVE_TIME);

            Config.DEFAULT_MEMORYSIZE = Integer.parseInt(xmlConfig.elementText("default-memorysize"));
            LOG.debug("Config Set default-memorysize " + Config.DEFAULT_MEMORYSIZE);
            return true;
        } catch (Exception e) {
            LOG.error("Config Not Correct", e);
            return false;
        }

    }
}
