package org.riseger.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;
import org.riseger.main.constant.Constant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class);

    public static String getText(File file) {
        try (FileReader reader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();

            if (bufferedReader.ready()) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error("Error reading", e);
        }
        return null;
    }

    public static void writeToFile(String content, String filePath) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content);
            writer.close();
            System.out.println("内容已成功写入文件。");
        } catch (IOException e) {
            System.err.println("写入文件时出现错误：" + e.getMessage());
        }
    }

    public static void writeToFile(byte[] content, String filePath) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(content);
            os.writeTo(Files.newOutputStream(Paths.get(filePath)));
            os.close();
            System.out.println("内容已成功写入文件。");
        } catch (IOException e) {
            System.err.println("写入文件时出现错误：" + e.getMessage());
        }
    }

    public static ByteBuf fileToByteBuf(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            ByteBuf buffer = Unpooled.buffer(in.available());
            while (in.available() > 0) {
                buffer.writeByte(in.read());
            }
            in.close();
            return buffer;
        } catch (Exception e) {
            LOG.error(e.getStackTrace());
        }
        return null;
    }

    public static String getNameFromFile(File file) {
        return file.getName().split("\\" + Constant.DOT_PREFIX)[0];
    }

    public static String getFIleName(String name, String style) {
        return name + "." + style;
    }

    public static String getClassLastDotName(Class<?> clazz) {
        return clazz.getCanonicalName().substring(clazz.getCanonicalName().lastIndexOf(".") + 1);
    }

    public static byte[] getBytes(File map) throws IOException {
        return Files.readAllBytes(Paths.get(map.toURI()));
    }

    public static File getFile(File dir, String name) throws IOException {
        File file = new File(dir + "/" + name);
        boolean res = true;
        if (!file.exists()) {
            res &= dir.mkdirs();
            res &= file.createNewFile();
        }
        return file;
    }

}
