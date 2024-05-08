package org.riseger.main.init;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class ConsoleInitialize extends Initializer {
    private static final Logger LOG = Logger.getLogger(ConsoleInitialize.class);

    public ConsoleInitialize(String rootPath) {
        super(rootPath);
    }

    private Scanner scanner;

    @Override
    public boolean init() {
        try {
            scanner = new Scanner(System.in);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to Initialize Console", e);
            throw e;
        }
    }

    public void join() {
        while (!scanner.nextLine().equals("exit")) {
        }
        scanner.close();
    }
}
