package org.riseger.main.system;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.log.LogDaemon;

public class LogSystem {
    public static LogSystem INSTANCE;

    private LogDaemon logDaemon;

    public void writeLog(CommandList commandList) {
        logDaemon.countDown(commandList.size());
        logDaemon.read();

    }
}
