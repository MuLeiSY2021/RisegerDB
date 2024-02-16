package org.riseger.main.system;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.log.LogDaemon;
import org.riseger.main.system.log.LogFileSystem;

public class LogSystem {
    public static LogSystem INSTANCE;

    private final LogDaemon logDaemon;

    private final LogFileSystem logFileSystem;

    public LogSystem(String rootPath) {
        this.logFileSystem = new LogFileSystem(rootPath);
        this.logDaemon = new LogDaemon();
    }

    public void writeLog(int sessionId,String dbName,String mpName, CommandList commandList) {
        logDaemon.countDown(commandList.size());
        logDaemon.read();
        logFileSystem.write(sessionId,dbName,mpName,commandList);
        logDaemon.unread();
    }

    public void deleteAllLog() {
        logFileSystem.deleteAll();
    }
}
