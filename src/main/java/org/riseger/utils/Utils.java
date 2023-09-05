package org.riseger.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utils {

    private static final Gson gson = new Gson();

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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public static List<?> toJson(String text, TypeToken<?> parameterized) {
        return gson.fromJson(text, parameterized.getType());
    }

    public static ByteBuf fileToByteBuf(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteBuf buffer = Unpooled.buffer(in.available());
        while (in.available() > 0) {
            buffer.writeByte(in.read());
        }
        return buffer;
    }
}
