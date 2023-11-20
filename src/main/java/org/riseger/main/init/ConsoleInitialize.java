package org.riseger.main.init;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class ConsoleInitialize extends Initializer {
    private static final Logger LOG = Logger.getLogger(ConsoleInitialize.class);

    public ConsoleInitialize(String rootPath) {
        super(rootPath);
    }

    @Override
    public boolean init() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (!scanner.nextLine().equals("exit")) {
            }
            return true;
        } catch (Exception e) {
            LOG.error("Failed to Initialize Console", e);
        }
        return false;
    }
}
