package org.risegerdb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils {

    public static String getText(File file) {
        try(FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();

            if(bufferedReader.ready()) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
