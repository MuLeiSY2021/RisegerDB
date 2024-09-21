package org.riseger.main.system;

import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.log.LogDaemon;
import org.riseger.main.system.log.LogFileSystem;

import java.io.File;
import java.util.List;

public class LogSystem {
    public static LogSystem INSTANCE;

    private final LogDaemon logDaemon;

    private final LogFileSystem logFileSystem;

    public LogSystem(String rootPath) {
        this.logFileSystem = new LogFileSystem(rootPath);
        this.logDaemon = new LogDaemon();
    }

    public static void setINSTANCE(LogSystem logSystem) {
        LogSystem.INSTANCE = logSystem;
    }

    public void init() {
        //Read all log file and run it
        File[] databases = StorageSystem.DEFAULT.getDatabasesFile();
        for (File database : databases) {
            List<File> logs = StorageSystem.DEFAULT.getLogFiles(database, true);
            for (File logFile : logs) {
                String sessionId = logFile.getName().split("\\.")[0].split("_")[1];
                CompileSystem.INSTANCE.processLog(logFile, Integer.parseInt(sessionId));
            }
        }
        //Start the log daemon
        Thread t = new Thread(logDaemon);
        t.setDaemon(true);
        t.start();
    }

    public void writeLog(int sessionId, String dbName, Function_c[] functions) {
        logDaemon.countDown(functions.length);
        logDaemon.read();
        logFileSystem.write(sessionId, dbName, functions);
        logDaemon.unread();
    }

    public void deleteAllLog() {
        logFileSystem.deleteAll();
    }
}
