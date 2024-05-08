package org.riseger.main.system.log;

import org.riseger.main.system.compile.compoent.CommandList;

import java.io.File;
import java.util.Objects;

public class LogFileSystem {

    public final String rootPath;

    public LogFileSystem(String rootPath) {
        this.rootPath = rootPath + "/data/databases";
    }

    public void deleteAll() {
        File rootFile = new File(rootPath);
        for (File db : Objects.requireNonNull(rootFile.listFiles())) {
            File logsDir = new File(db.getPath() + "/logs");
            for (File log : Objects.requireNonNull(logsDir.listFiles())) {
                log.delete();
            }
        }
    }

    public void write(int sessionId, String dbName, CommandList commandList) {
        LogFile logFile = new LogFile(rootPath, sessionId, dbName);
        logFile.write(commandList);
    }
}
