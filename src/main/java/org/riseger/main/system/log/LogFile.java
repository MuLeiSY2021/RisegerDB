package org.riseger.main.system.log;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogFile {

    private final File logDirectory;



    private static final Logger LOG = Logger.getLogger(LogFile.class);
    private final File log;

    public LogFile(String rootPath, int sessionId, String dbName) {
        this.logDirectory = new File(rootPath + "/" + dbName + ".db/" + "logs");
        this.log = new File(rootPath + "/" + dbName + ".db/" + "logs/" + sessionId + ".log");
    }

    public void write(CommandList commandList) {
        try {
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }
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