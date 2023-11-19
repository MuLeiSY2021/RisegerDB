package org.riseger.main.init;

import java.util.Scanner;

public class ConsoleInitialize implements Initializer {
    @Override
    public void init() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (!scanner.nextLine().equals("exit")) {
        }
    }
}
