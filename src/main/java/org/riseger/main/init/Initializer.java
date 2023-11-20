package org.riseger.main.init;

public abstract class Initializer {
    protected final String rootPath;

    public Initializer(String rootPath) {
        this.rootPath = rootPath;
    }

    abstract boolean init() throws Exception;
}
