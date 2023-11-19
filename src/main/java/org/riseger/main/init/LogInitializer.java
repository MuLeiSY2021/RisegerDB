package org.riseger.main.init;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class LogInitializer {
    public static final Logger LOG = Logger.getLogger("LogInitializer");

    private final String rootPath;

    public LogInitializer(String rootPath) {
        this.rootPath = rootPath;
    }

    public void initLog() {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream(rootPath + "/resources/log4j.properties");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
