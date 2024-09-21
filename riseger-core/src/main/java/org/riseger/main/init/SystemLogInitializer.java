package org.riseger.main.init;

import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class SystemLogInitializer extends Initializer {
    public static final Logger LOG = Logger.getLogger("SystemLogInitializer");


    public SystemLogInitializer(String rootPath) {
        super(rootPath);
    }

    public boolean init() throws IOException {
        FileInputStream fileInputStream = null;
        try {
            Properties properties = new Properties();
            fileInputStream = new FileInputStream(rootPath + "/configs/log4j.properties");
            properties.load(fileInputStream);
            PropertyConfigurator.configure(properties);
            return true;
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            throw e;
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
