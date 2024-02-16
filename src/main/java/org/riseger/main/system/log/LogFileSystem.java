package org.riseger.main.system.log;

import org.riseger.main.system.compile.compoent.CommandList;

import java.io.File;

public class LogFileSystem {

    public final String rootPath;

    public LogFileSystem(String rootPath) {
        this.rootPath = rootPath;
    }

    public void deleteAll() {
        //TODO: 删除所有日志
    }

    public void write(int sessionId,String dbName,String mpName, CommandList commandList) {
        //TODO: 写入日志
        File file = new File(rootPath + "/" + dbName + "/" + mpName + "/" + sessionId + ".log");

    }
}
