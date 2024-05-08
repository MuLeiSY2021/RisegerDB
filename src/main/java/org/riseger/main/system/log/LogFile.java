package org.riseger.main.system.log;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {

    private final File log;

    private static final Logger LOG = Logger.getLogger(LogFile.class);

    public LogFile(String rootPath, int sessionId, String dbName) {
        this.log = new File(rootPath + "/" + dbName + "/" + "logs" + sessionId + ".log");
    }

    public void write(CommandList commandList) {
        try {
            if (!log.exists()) {
                log.createNewFile();
            }

            if (!log.isFile()) {
                throw new IOException(log.getPath() + " is not a file.");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(log, true))) {
                //TODO: CommandList能被序列化吗？
                writer.write(Utils.toJson(commandList) + "\n");
                LOG.info("Data has been written to " + log.getName());
            } catch (IOException e) {
                LOG.error("Error writing to file: " + e.getMessage());
                // Log or handle the error appropriately
            }
        } catch (IOException e) {
            LOG.error("Error creating or accessing file: " + e.getMessage());
            // Log or handle the error appropriately
        }
    }
}